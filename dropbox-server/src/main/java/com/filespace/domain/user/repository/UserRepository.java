package com.filespace.domain.user.repository;

import com.filespace.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByNickname(String nickname);


    Boolean existsByEmail(String email);

    User findByEmail(String Email);

}