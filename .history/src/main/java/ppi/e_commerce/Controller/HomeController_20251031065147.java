package ppi.e_commerce.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ppi.e_commerce.Service.*;
import ppi.e_commerce.Model.Product;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandService brandService;

    @GetMapping("/")
    public String home(Model model) {
        // Obtener productos activos desde la base de datos
        List<Product> allProducts = productService.findActiveProducts();
        
        // Limitar a los primeros 8 productos para la página principal
        List<Product> featuredProducts = allProducts.stream()
            .limit(8)
            .collect(Collectors.toList());
        
        model.addAttribute("products", featuredProducts);
        model.addAttribute("categories", categoryService.findActiveCategories());
        model.addAttribute("brands", brandService.findActiveBrands());
        
        return "index";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }
}
