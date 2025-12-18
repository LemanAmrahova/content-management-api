package com.leman.contentmanagementapi.repository;

import com.leman.contentmanagementapi.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {

    @Modifying
    @Query("UPDATE Category c SET c.active = false WHERE c.id = :id")
    void deactivateById(Long id);

}
