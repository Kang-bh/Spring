package com.filespace.domain.user.service;

import com.filespace.domain.user.domain.User;
import com.filespace.domain.user.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserIdService {
    private final UserRepository userRepository;

    public UserIdService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Long getCurrentUserId() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        User data = userRepository.findByEmail(name);
        return data.getId();
    }
}
