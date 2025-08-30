package org.softuni.onlinegrocery.repository;

import org.softuni.onlinegrocery.domain.entities.ProductNew;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductNewRepository extends JpaRepository<ProductNew, String> {

    @Query("SELECT p FROM ProductNew p WHERE p.subcategory.id = :subcategoryId AND p.isActive = true ORDER BY p.name")
    List<ProductNew> findBySubcategoryIdAndIsActiveTrueOrderByName(@Param("subcategoryId") String subcategoryId);

    @Query("SELECT p FROM ProductNew p WHERE p.subcategory.categoryNew.id = :categoryId AND p.isActive = true ORDER BY p.name")
    List<ProductNew> findByCategoryNewIdAndIsActiveTrueOrderByName(@Param("categoryId") String categoryId);

    @Query("SELECT p FROM ProductNew p WHERE p.name LIKE %:name% AND p.isActive = true ORDER BY p.name")
    List<ProductNew> findByNameContainingAndIsActiveTrueOrderByName(@Param("name") String name);

    @Query("SELECT p FROM ProductNew p WHERE p.brand = :brand AND p.isActive = true ORDER BY p.name")
    List<ProductNew> findByBrandAndIsActiveTrueOrderByName(@Param("brand") String brand);

    @Query("SELECT p FROM ProductNew p WHERE p.price BETWEEN :minPrice AND :maxPrice AND p.isActive = true ORDER BY p.price")
    List<ProductNew> findByPriceBetweenAndIsActiveTrueOrderByPrice(@Param("minPrice") BigDecimal minPrice, @Param("maxPrice") BigDecimal maxPrice);

    @Query("SELECT p FROM ProductNew p WHERE p.quantity > 0 AND p.isActive = true ORDER BY p.name")
    List<ProductNew> findAvailableProductsOrderByName();

    @Query("SELECT p FROM ProductNew p WHERE p.isActive = true ORDER BY p.name")
    List<ProductNew> findByIsActiveTrueOrderByName();

    @Query("SELECT DISTINCT p.brand FROM ProductNew p WHERE p.isActive = true ORDER BY p.brand")
    List<String> findDistinctBrands();

    Optional<ProductNew> findByNameAndSubcategoryId(String name, String subcategoryId);

    boolean existsByNameAndSubcategoryId(String name, String subcategoryId);
}