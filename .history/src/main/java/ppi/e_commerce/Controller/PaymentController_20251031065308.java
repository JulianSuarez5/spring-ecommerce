package ppi.e_commerce.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ppi.e_commerce.Model.Order;
import ppi.e_commerce.Model.Payment;
// INICIO DE LA CORRECCIÓN: Importaciones necesarias
import ppi.e_commerce.Model.User;
import ppi.e_commerce.Model.CartItem;
import ppi.e_commerce.Service.CartService;
import ppi.e_commerce.Repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.List;
import java.util.Optional;
// FIN DE LA CORRECCIÓN
import ppi.e_commerce.Service.PaymentService;
import ppi.e_commerce.Service.OrderService;

import java.util.Map;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;
    @Autowired
    private OrderService orderService;

    // INICIO DE LA CORRECCIÓN: Inyectar servicios de Carrito y Usuario
    @Autowired
    private CartService cartService;
    
    @Autowired
    private UserRepository userRepository;
    // FIN DE LA CORRECCIÓN

    @GetMapping("/checkout")
    public String checkoutPage(Model model) {
        // INICIO DE LA CORRECCIÓN: Lógica para cargar el carrito
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return "redirect:/login";
        }

        String username = auth.getName();
        Optional<User> optUser = userRepository.findByUsername(username);
        if (optUser.isEmpty()) {
            return "redirect:/login";
        }
        User currentUser = optUser.get();

        List<CartItem> cartItems = cartService.getCartItems(currentUser);
        if (cartItems.isEmpty()) {
            // No se puede hacer checkout si el carrito está vacío
            return "redirect:/cart";
        }
        
        double cartTotal = cartService.getCartTotal(currentUser);
        int itemCount = cartService.getCartItemCount(currentUser);
        
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("cartTotal", cartTotal);
        model.addAttribute("itemCount", itemCount);
        // FIN DE LA CORRECCIÓN
        
        return "payment/checkout";
    }

    @PostMapping("/create-paypal")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> createPayPalPayment(@RequestParam Double amount) {
        try {
            // This would typically create a PayPal payment
            Map<String, Object> response = Map.of(
                "success", true,
                "paymentId", "PAYPAL_" + System.currentTimeMillis(),
                "amount", amount,
                "currency", "USD"
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/confirm-paypal")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> confirmPayPalPayment(@RequestParam String paymentId, 
                                                                   @RequestParam String payerId) {
        try {
            Payment payment = paymentService.processPayPalPayment(paymentId, payerId);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "paymentId", payment.getId(),
                "status", payment.getStatus()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/cash-on-delivery")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> processCashOnDelivery(@RequestParam Integer orderId) {
        try {
            Order order = orderService.getOrderById(orderId).orElse(null);
            if (order != null) {
                Payment payment = paymentService.processCashOnDelivery(order);
                return ResponseEntity.ok(Map.of(
                    "success", true,
                    "paymentId", payment.getId(),
                    "status", payment.getStatus()
                ));
            }
            return ResponseEntity.badRequest().body(Map.of("error", "Order not found"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Stripe webhook removed - only PayPal is supported

    @GetMapping("/success")
    public String paymentSuccess(@RequestParam(required = false) String payment_intent, Model model) {
        if (payment_intent != null) {
            Payment payment = paymentService.getPaymentById(payment_intent);
            model.addAttribute("payment", payment);
        }
        return "payment/success";
    }

    @GetMapping("/cancel")
    public String paymentCancel() {
        return "payment/cancel";
    }
}
