package ppi.e_commerce.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ppi.e_commerce.Model.Product;
import ppi.e_commerce.Model.User;
import ppi.e_commerce.Service.ProductService;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final Logger log = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService product_service;

    @GetMapping("")
    public String show(Model model) {
        model.addAttribute("products", product_service.findAll());
        return "Products/show";
    }

    @GetMapping("/create")
    public String create() {
        return "Products/create";
    }

    @PostMapping("/save")
    public String saveProduct(Product product) {
        log.info("Este es el objeto de la vista: {}", product);
        User user = new User(1, "", "", "", "", "", "", "");
        product.setUser(user);

        product_service.saveProduct(product);
        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        Product product = new Product();
        Optional<Product> optionalProduct = product_service.getProductById(id);
        product = optionalProduct.get();
        model.addAttribute("product", product);

        log.info("Producto buscado: {}", product);

        return "Products/edit";
    }

    @PostMapping("/update")
    public String update (Product product){

        product_service.updateProduct(product);

        return "redirect:/products";
    }

    @GetMapping("/delete/{id}")
    public String delete (@PathVariable Integer id){

        product_service.deleteProduct(id);

        return "redirect:/products";
    }
}
