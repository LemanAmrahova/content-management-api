package com.leman.contentmanagementapi.specification;

import com.leman.contentmanagementapi.dto.request.ArticleFilterRequest;
import com.leman.contentmanagementapi.entity.Article;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import static com.leman.contentmanagementapi.specification.BaseSpecification.combineWithAnd;
import static com.leman.contentmanagementapi.specification.BaseSpecification.equalIfNotNull;
import static com.leman.contentmanagementapi.specification.BaseSpecification.likeIfNotBlank;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ArticleSpecification {

    public static Specification<Article> getSpecification(ArticleFilterRequest request) {
        return (root, query, cb) -> combineWithAnd(cb,
                equalIfNotNull(cb, root.get("published"), request.getPublished()),
                equalIfNotNull(cb, root.get("active"), request.getActive()),
                equalIfNotNull(cb, root.get("category").get("id"), request.getCategoryId()),
                likeIfNotBlank(cb, root.get("title"), request.getTitle()),
                likeIfNotBlank(cb, root.get("content"), request.getContent())
        );
    }

}
