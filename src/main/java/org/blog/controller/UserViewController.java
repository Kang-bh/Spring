package org.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserViewController {

    @GetMapping("/login")
    public String login() {
        System.out.println("login");
        return "login";
    }

    @GetMapping("/signup")
    public String signup() {
        System.out.println("signup");
        return "signup";
    }
}
