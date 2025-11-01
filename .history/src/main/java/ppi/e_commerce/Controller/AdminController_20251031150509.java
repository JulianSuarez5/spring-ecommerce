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
import ppi.e_commerce.Repository.UserRepository;

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
    private UserRepository userRepository;
    @Autowired
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @GetMapping
    public String adminDashboard(Model model, Authentication authentication) {
        // Verificar que el usuario tenga rol de administrador
        if (authentication == null || !authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            return "redirect:/login?error=access_denied";
        }

        // --- INICIO DE LA MEJORA ---

        // 1. KPIs Principales (Métricas Clave de Rendimiento)
        Double totalSales = orderService.getTotalSales(); [cite_start]// Obtenemos ventas totales [cite: 412, 862]
        model.addAttribute("totalSales", (totalSales != null) ? totalSales : 0.0);
        model.addAttribute("totalOrders", orderService.countOrders()); [cite_start]// Contamos pedidos [cite: 413, 699]

        // 2. Métricas Secundarias (Inventario)
        [cite_start]model.addAttribute("totalProducts", productService.countProducts()); [cite: 698, 464]
        [cite_start]model.addAttribute("totalCategories", categoryService.countCategories()); [cite: 699, 350]
        [cite_start]model.addAttribute("totalBrands", brandService.countBrands()); [cite: 699, 313]

        // 3. Contenido Dinámico (Actividad)
        [cite_start]// Obtenemos los 5 pedidos más recientes [cite: 413, 857]
        model.addAttribute("recentOrders", orderService.getRecentOrders(5)); 
        
        // --- FIN DE LA MEJORA ---

        [cite_start]return "Admin/dashboard"; [cite: 699]
    }

    @GetMapping("/login")
    public String adminLogin() {
        // standalone admin login page
        [cite_start]return "auth/admin_login"; [cite: 699]
    }

    @GetMapping("/users")
    public String listUsers(Model model, Authentication authentication) {
        if (authentication == null || !authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            [cite_start]return "redirect:/login?error=access_denied"; [cite: 700]
        }

        [cite_start]model.addAttribute("users", userRepository.findAll()); [cite: 701]
        [cite_start]return "Admin/users"; [cite: 701]
    }

    @GetMapping("/users/create")
    public String createUserForm(Model model, Authentication authentication) {
        if (authentication == null || !authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            [cite_start]return "redirect:/login?error=access_denied"; [cite: 702]
        }

        [cite_start]model.addAttribute("user", new ppi.e_commerce.Model.User()); [cite: 703]
        [cite_start]return "Admin/user_form"; [cite: 703]
    }

    @PostMapping("/users/create")
    public String createUser(@org.springframework.web.bind.annotation.ModelAttribute ppi.e_commerce.Model.User user, Model model, Authentication authentication) {
        if (authentication == null || !authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            [cite_start]return "redirect:/login?error=access_denied"; [cite: 704]
        }

        [cite_start]if (userRepository.findByUsername(user.getUsername()).isPresent()) { [cite: 705]
            [cite_start]model.addAttribute("error", "El nombre de usuario ya existe"); [cite: 705]
            [cite_start]model.addAttribute("user", user); [cite: 706]
            [cite_start]return "Admin/user_form"; [cite: 706]
        }

        [cite_start]user.setPassword(passwordEncoder.encode(user.getPassword())); [cite: 706]
        [cite_start]if (user.getRole() == null || user.getRole().isBlank()) { [cite: 707]
            [cite_start]user.setRole("USER"); [cite: 707]
        }
        [cite_start]userRepository.save(user); [cite: 708]
        [cite_start]return "redirect:/admin/users"; [cite: 708]
    }

    @GetMapping("/orders")
    public String adminOrders(Model model) {
        [cite_start]model.addAttribute("orders", orderService.getAllOrders()); [cite: 709]
        [cite_start]return "Admin/orders/index"; [cite: 709]
    }

    @GetMapping("/profile")
    public String adminProfile() {
        [cite_start]return "Admin/profile"; [cite: 710]
    }

    @GetMapping("/users/edit/{id}")
    public String editUser(@PathVariable Integer id, Model model) {
        [cite_start]User user = userRepository.findById(id).orElse(null); [cite: 710]
        [cite_start]model.addAttribute("user", user); [cite: 711]
        [cite_start]return "Admin/users/edit"; [cite: 711]
    }

    @PostMapping("/users/update/{id}")
    public String updateUser(@PathVariable Integer id, @ModelAttribute User user) {
        [cite_start]User existing = userRepository.findById(id).orElse(null); [cite: 711]
        [cite_start]if (existing != null) { [cite: 712]
            [cite_start]existing.setUsername(user.getUsername()); [cite: 712]
            [cite_start]existing.setEmail(user.getEmail()); [cite: 712]
            [cite_start]existing.setRole(user.getRole()); [cite: 712]
            [cite_start]existing.setActive(user.isActive()); [cite: 712]
            [cite_start]userRepository.save(existing); [cite: 712]
        }
        [cite_start]return "redirect:/admin/users"; [cite: 713]
    }
}