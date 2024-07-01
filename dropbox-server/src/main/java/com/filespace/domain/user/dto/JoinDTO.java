package com.filespace.domain.user.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.plaf.multi.MultiListUI;
import java.util.Optional;

@Getter
@Setter
public class JoinDTO {

    private String nickname;

    private String password;

    private String email;

    private MultipartFile image;

}
