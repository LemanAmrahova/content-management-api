package com.leman.contentmanagementapi.repository;

import com.leman.contentmanagementapi.entity.Category;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {

    List<Category> findAllByActiveTrue();

    Optional<Category> findByIdAndActiveTrue(Long id);

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);

}
