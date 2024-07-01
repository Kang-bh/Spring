package com.filespace.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MyInfoResponseDTO {
    private Long id;
    private  String email;
    private  String nickname;
//    private  String password;
    private  String profile_image;

//    public MyInfoResponseDTO (UserEntity userEntity){
//        this.id = userEntity.getId();
//        this.nickname = userEntity.getNickname();
//        this.email = userEntity.getEmail();
//        this.password = userEntity.getPassword();
//        this.profile_image = userEntity.getProfile_image();
//    }

}
