package com.filespace.domain.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class LogoutController {
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return null;
    }
}
