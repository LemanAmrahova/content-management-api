package com.leman.contentmanagementapi.entity;

import com.leman.contentmanagementapi.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Table(name = "users")
public class User extends BaseEntity {

    @NotNull
    @Column(nullable = false, unique = true)
    private String username;

    @NotNull
    @Column(nullable = false, unique = true)
    private String email;

    @NotNull
    @Column(nullable = false)
    private String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private Role role = Role.USER;

    @NotNull
    @Column(nullable = false)
    private boolean enabled = true;

}
