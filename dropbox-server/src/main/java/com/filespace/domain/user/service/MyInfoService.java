package com.filespace.domain.user.service;

import com.filespace.domain.user.dto.MyInfoResponseDTO;
import com.filespace.domain.user.dto.ResultDTO;
import com.filespace.domain.user.domain.User;
import com.filespace.domain.user.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class MyInfoService {
    private final UserRepository userRepository;

    public  MyInfoService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResultDTO<MyInfoResponseDTO> getMyInfo() {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println(email);
        User data = userRepository.findByEmail(email);

        if(data != null) {
            MyInfoResponseDTO myInfoResponseDTO = convertToMyInfoResponseDTO(data);
            System.out.println(myInfoResponseDTO);
            return new ResultDTO<>("success", myInfoResponseDTO);

        }
        else {
            return new ResultDTO<>("error", "Member not found");
        }

    }
    private MyInfoResponseDTO convertToMyInfoResponseDTO(User user) {
        MyInfoResponseDTO myInfoResponseDTO = new MyInfoResponseDTO();
        myInfoResponseDTO.setId(user.getId());
        myInfoResponseDTO.setEmail(user.getEmail());
        myInfoResponseDTO.setNickname(user.getNickname());
        myInfoResponseDTO.setProfile_image(user.getProfile_image());

        // 기타 필드 설정

        return myInfoResponseDTO;
    }



}
