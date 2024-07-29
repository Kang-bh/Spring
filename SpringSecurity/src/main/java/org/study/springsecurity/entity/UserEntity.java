package org.study.springsecurity.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Table(name="users")
@Getter
@Setter
@Entity
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false)
    private String username;

    private String password;

    private String role;
}
