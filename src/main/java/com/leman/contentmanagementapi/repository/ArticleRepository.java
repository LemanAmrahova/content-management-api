package com.leman.contentmanagementapi.repository;

import com.leman.contentmanagementapi.entity.Article;
import com.leman.contentmanagementapi.projection.ArticleDetailProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long>, JpaSpecificationExecutor<Article> {

    @EntityGraph(attributePaths = {"category"})
    Page<Article> findAll(Specification<Article> spec, Pageable pageable);

    @Query("""
        select
            a.id as id,
            c.id as categoryId,
            c.name as categoryName,
            a.title as title,
            a.content as content,
            a.active as active,
            a.published as published,
            a.createdAt as createdAt,
            a.updatedAt as updatedAt
        from Article a
        join a.category c
        where a.id = :id and a.active = true
    """)
    Optional<ArticleDetailProjection> findByIdAndActiveWithCategory(Long id);

    Optional<Article> findByIdAndActiveTrue(Long id);

}
