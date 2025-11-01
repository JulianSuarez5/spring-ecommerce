package ppi.e_commerce.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ppi.e_commerce.Model.Brand;
import ppi.e_commerce.Repository.BrandRepository;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BrandService {

    @Autowired
    private BrandRepository brandRepository;

    public List<Brand> findAll() {
        return brandRepository.findAll();
    }

    public List<Brand> findAllActive() {
        return brandRepository.findByActiveTrue();
    }

    public List<Brand> findAllActiveOrderByName() {
        return brandRepository.findByActiveTrueOrderByNameAsc();
    }

    public Optional<Brand> findById(Integer id) {
        return brandRepository.findById(id);
    }

    @Transactional
    public Brand saveBrand(Brand brand) {
        return brandRepository.save(brand);
    }

    @Transactional
    public Brand updateBrand(Brand brand) {
        return brandRepository.save(brand);
    }

    @Transactional
    public void deleteBrand(Integer id) {
        Optional<Brand> brand = brandRepository.findById(id);
        brand.ifPresent(b -> {
            b.setActive(false);
            brandRepository.save(b);
        });
    }

    public boolean existsByName(String name) {
        return brandRepository.existsByName(name);
    }
}