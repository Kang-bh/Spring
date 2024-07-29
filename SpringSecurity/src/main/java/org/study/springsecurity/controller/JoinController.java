package org.study.springsecurity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.study.springsecurity.dto.JoinDTO;
import org.study.springsecurity.service.JoinService;

@Controller
public class JoinController {

    @Autowired // todo : 필드 주입 방식, 나중에 생성자 주입 방식 사용
    private JoinService joinService;

    @GetMapping("/join")
    public String join(){
        return "join";
    }

    @PostMapping("/joinProc")
    public String joinProcess(JoinDTO joinDTO) {

        System.out.println(joinDTO.getUsername());
        System.out.println(joinDTO.getPassword());

        joinService.JoinProcess(joinDTO);

        return "redirect:/login"; // to login page
    }

}
