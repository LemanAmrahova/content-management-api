package com.leman.contentmanagementapi.specification;

import com.leman.contentmanagementapi.dto.request.ArticleFilterRequest;
import com.leman.contentmanagementapi.entity.Article;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ArticleSpecification extends BaseSpecification {

    public static Specification<Article> getSpecification(ArticleFilterRequest request) {
        return (root, query, cb) -> {
            //TODO: Continue
        };
    }

}
