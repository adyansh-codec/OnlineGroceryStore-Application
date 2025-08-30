package org.softuni.onlinegrocery.repository;

import org.softuni.onlinegrocery.domain.entities.Subcategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubcategoryRepository extends JpaRepository<Subcategory, String> {

    @Query("SELECT s FROM Subcategory s WHERE s.categoryNew.id = :categoryId ORDER BY s.name")
    List<Subcategory> findByCategoryNewIdOrderByName(@Param("categoryId") String categoryId);

    @Query("SELECT s FROM Subcategory s WHERE s.categoryNew.name = :categoryName ORDER BY s.name")
    List<Subcategory> findByCategoryNewNameOrderByName(@Param("categoryName") String categoryName);

    Optional<Subcategory> findByName(String name);

    @Query("SELECT s FROM Subcategory s LEFT JOIN FETCH s.products WHERE s.id = :id")
    Optional<Subcategory> findByIdWithProducts(@Param("id") String id);

    @Query("SELECT DISTINCT s FROM Subcategory s LEFT JOIN FETCH s.products WHERE s.categoryNew.id = :categoryId")
    List<Subcategory> findByCategoryNewIdWithProducts(@Param("categoryId") String categoryId);

    boolean existsByNameAndCategoryNewId(String name, String categoryId);
}
