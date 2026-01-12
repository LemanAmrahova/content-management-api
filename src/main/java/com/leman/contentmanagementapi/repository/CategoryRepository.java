package com.leman.contentmanagementapi.repository;

import com.leman.contentmanagementapi.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {

    List<Category> findAllByActiveTrue();

    Optional<Category> findByIdAndActiveTrue(Long id);

    @Modifying
    @Query("UPDATE Category c SET c.active = false WHERE c.id = :id")
    void deactivateById(Long id);

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);

}
