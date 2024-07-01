package com.filespace.domain.user.controller;

import com.filespace.domain.user.dto.MyInfoResponseDTO;
import com.filespace.domain.user.dto.ResultDTO;
import com.filespace.domain.user.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class LoginController {
    UserRepository userRepository;

    public LoginController(UserRepository userRepository) {
        this.userRepository = userRepository;
        //this.userEntity = userEntity;
    }

    @PostMapping("/login")
    public ResponseEntity<ResultDTO<MyInfoResponseDTO>> login(@RequestParam("email") String email, @RequestParam("password") String password) {
        
        return null;
    }
}
