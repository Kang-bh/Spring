package com.filespace.domain.user.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Table(name="user_refresh token")
@Setter
@Getter
@ToString
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Refresh {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id", updatable = false)
    private Long id;

    @Column(name="email")
    private String email;

    @Column(name="refresh")
    private String refresh;

    @Column(name="expiration")
    private String expiration;
}
