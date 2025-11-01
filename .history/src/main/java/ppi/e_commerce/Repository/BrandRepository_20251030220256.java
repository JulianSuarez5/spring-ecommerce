package ppi.e_commerce.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ppi.e_commerce.Model.Brand;
import java.util.List;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Integer> {
    List<Brand> findByActiveTrue();
    List<Brand> findByActiveTrueOrderByNameAsc();
    boolean existsByName(String name);
}