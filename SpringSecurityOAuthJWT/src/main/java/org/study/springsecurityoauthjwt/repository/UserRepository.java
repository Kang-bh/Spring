package org.study.springsecurityoauthjwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.study.springsecurityoauthjwt.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByUsername(String username);
}