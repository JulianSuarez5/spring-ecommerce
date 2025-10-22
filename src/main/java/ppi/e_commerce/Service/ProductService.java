package ppi.e_commerce.Service;

import ppi.e_commerce.Model.Product;
import java.util.Optional;

public interface ProductService {
    public Product saveProduct(Product product);
    public Optional<Product> getProductById(Integer id);
    public void updateProduct(Product product);
    public void deleteProduct(Integer id);
}
