package com.leman.contentmanagementapi.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import java.util.Objects;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BaseSpecification {

    public static <T> Predicate equalIfNotNull(CriteriaBuilder cb, Path<T> path, T value) {
        return value == null ? null : cb.equal(path, value);
    }

    public static Predicate likeIfNotBlank(CriteriaBuilder cb, Path<String> path, String value) {
        return (value == null || value.isBlank()) ? null : cb.like(cb.lower(path), "%" + value.toLowerCase() + "%");
    }

    public static Predicate combineWithAnd(CriteriaBuilder cb, Predicate... predicates) {
        return cb.and(Stream.of(predicates)
                .filter(Objects::nonNull)
                .toArray(Predicate[]::new)
        );
    }

}
