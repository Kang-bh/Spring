package org.study.springsecurityoauthjwt.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="user")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", nullable = false, updatable = false)
    private Long id;

    @Column(name="username", nullable = false)
    private String username;

    @Column(name="name", nullable = false)
    private String name;

    @Column(name="email")
    private String email;

    @Column(name="role")
    private String role;
}