package org.softuni.onlinegrocery.repository;

import org.softuni.onlinegrocery.domain.entities.CategoryNew;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryNewRepository extends JpaRepository<CategoryNew, String> {

    Optional<CategoryNew> findByName(String name);

    @Query("SELECT c FROM CategoryNew c ORDER BY c.name")
    List<CategoryNew> findAllOrderByName();

    @Query("SELECT c FROM CategoryNew c LEFT JOIN FETCH c.subcategories WHERE c.id = :id")
    Optional<CategoryNew> findByIdWithSubcategories(@Param("id") String id);

    @Query("SELECT DISTINCT c FROM CategoryNew c LEFT JOIN FETCH c.subcategories")
    List<CategoryNew> findAllWithSubcategories();

    boolean existsByName(String name);
}
