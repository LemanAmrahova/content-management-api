package com.leman.contentmanagementapi.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import java.util.List;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Table(name = "category")
public class Category extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "is_active", nullable = false)
    private Boolean active;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Article> articles;

}
