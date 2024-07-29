package org.study.springsecurity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.study.springsecurity.dto.JoinDTO;
import org.study.springsecurity.entity.UserEntity;
import org.study.springsecurity.repository.UserRepository;

@Service
public class JoinService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public void JoinProcess(JoinDTO joinDTO) {

        // db 동일 유저 네임 존재 확인
        // todo: 정규식 처리
        boolean isUser = userRepository.existsByUsername(joinDTO.getUsername());
        if (isUser)
            return;

        UserEntity user = new UserEntity(); // 빈 객체 생성

        user.setUsername(joinDTO.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(joinDTO.getPassword()));
        user.setRole("ROLE_USER");

        userRepository.save(user);
    }

}
