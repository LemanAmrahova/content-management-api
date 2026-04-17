package com.leman.contentmanagementapi.repository;

import com.leman.contentmanagementapi.entity.Article;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long>, JpaSpecificationExecutor<Article> {

    @Override
    @EntityGraph(attributePaths = {"category"})
    Page<Article> findAll(Specification<Article> spec, Pageable pageable);

    @EntityGraph("category")
    Optional<Article> findByIdAndActiveTrue(Long id);

}
