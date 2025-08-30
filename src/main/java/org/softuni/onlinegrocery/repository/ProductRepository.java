package org.softuni.onlinegrocery.repository;

import org.softuni.onlinegrocery.domain.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    Optional<Product> findByName(String name);

     // Fetch all products by category ID
   // @Query("SELECT p FROM Product p WHERE p.category.id = :categoryId")
   // List<Product> findProductsByCategoryId(@Param("categoryId") Long categoryId);
}
