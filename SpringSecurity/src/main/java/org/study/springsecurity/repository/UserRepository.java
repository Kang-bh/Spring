package org.study.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.study.springsecurity.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    boolean existsByUsername(String username);
    UserEntity findByUsername(String username);
}
