package com.filespace.domain.user.controller;

import com.filespace.domain.folder.service.FolderService;
import com.filespace.domain.user.repository.UserRepository;
import com.filespace.domain.user.service.WithdrawalService;
import org.springframework.expression.AccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@ResponseBody
@Controller
public class WithdrawalController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final WithdrawalService withdrawalService;

    WithdrawalController(UserRepository userRepository, PasswordEncoder passwordEncoder, WithdrawalService withdrawalService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.withdrawalService = withdrawalService;

    }
    @PostMapping("/Withdrawal")
    public ResponseEntity<?> memberWithdrawal (@RequestParam("password") String inputPassword) {

        try {
            withdrawalService.withdrawalProcess(inputPassword);
            return ResponseEntity.ok("회원탈퇴가 성공적으로 처리되었습니다.");
        } catch (WithdrawalService.PasswordNotMachedException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }





    }

}
