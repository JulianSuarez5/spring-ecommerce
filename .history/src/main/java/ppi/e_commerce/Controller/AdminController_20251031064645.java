package ppi.e_commerce.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import ppi.e_commerce.Service.*;
import ppi.e_commerce.Model.User;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private  userRepository;

    @Autowired
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @GetMapping
    public String adminDashboard(Model model, Authentication authentication) {
        // Verificar que el usuario tenga rol de administrador
        if (authentication == null || !authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            return "redirect:/login?error=access_denied";
        }

        // Estadísticas para el dashboard
        model.addAttribute("totalProducts", productService.countProducts());
        model.addAttribute("totalCategories", categoryService.countCategories());
        model.addAttribute("totalBrands", brandService.countBrands());
        model.addAttribute("totalOrders", orderService.countOrders());

        return "Admin/dashboard";
    }

    @GetMapping("/login")
    public String adminLogin() {
        // standalone admin login page
        return "auth/admin_login";
    }

    @GetMapping("/users")
    public String listUsers(Model model, Authentication authentication) {
        if (authentication == null || !authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            return "redirect:/login?error=access_denied";
        }

        model.addAttribute("users", userRepository.findAll());
        return "Admin/users";
    }

    @GetMapping("/users/create")
    public String createUserForm(Model model, Authentication authentication) {
        if (authentication == null || !authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            return "redirect:/login?error=access_denied";
        }

        model.addAttribute("user", new ppi.e_commerce.Model.User());
        return "Admin/user_form";
    }

    @PostMapping("/users/create")
    public String createUser(@org.springframework.web.bind.annotation.ModelAttribute ppi.e_commerce.Model.User user, Model model, Authentication authentication) {
        if (authentication == null || !authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            return "redirect:/login?error=access_denied";
        }

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            model.addAttribute("error", "El nombre de usuario ya existe");
            model.addAttribute("user", user);
            return "Admin/user_form";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRole() == null || user.getRole().isBlank()) {
            user.setRole("USER");
        }
        userRepository.save(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/orders")
public String adminOrders(Model model) {
    model.addAttribute("orders", orderService.getAllOrders());
    return "Admin/orders/index";
}

@GetMapping("/profile")
public String adminProfile() {
    return "Admin/profile";
}

@GetMapping("/users/edit/{id}")
public String editUser(@PathVariable Integer id, Model model) {
    User user = userRepository.findById(id).orElse(null);
    model.addAttribute("user", user);
    return "Admin/users/edit";
}

@PostMapping("/users/update/{id}")
public String updateUser(@PathVariable Integer id, @ModelAttribute User user) {
    User existing = userRepository.findById(id).orElse(null);
    if (existing != null) {
        existing.setUsername(user.getUsername());
        existing.setEmail(user.getEmail());
        existing.setRole(user.getRole());
        existing.setActive(user.isActive());
        userRepository.save(existing);
    }
    return "redirect:/admin/users";
}
}
