package ppi.e_commerce.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ppi.e_commerce.Model.Product;

import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

}
