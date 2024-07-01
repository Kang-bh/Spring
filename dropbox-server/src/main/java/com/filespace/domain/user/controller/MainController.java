package com.filespace.domain.user.controller;

import com.filespace.domain.user.service.UserIdService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class MainController {
    UserIdService userIdService;
    public MainController(UserIdService userIdService) {
        this.userIdService = userIdService;
    }

    @Tag(name = "@Id id", description = "다른 테이블에서 에서 user_id 가져오기 위함")
    @GetMapping("/")
    public String mainP() {

        Long id = userIdService.getCurrentUserId();

        return " 현재 로그인된 유저 아이디(식별값 int) : " + id;
    }
}