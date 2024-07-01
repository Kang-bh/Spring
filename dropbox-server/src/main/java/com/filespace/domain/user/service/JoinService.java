package com.filespace.domain.user.service;

import com.filespace.domain.user.dto.JoinDTO;
import com.filespace.domain.user.dto.MyInfoResponseDTO;
import com.filespace.domain.user.dto.ResultDTO;
import com.filespace.domain.user.domain.User;
import com.filespace.domain.user.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class JoinService {
    private final UserRepository userRepository;
    private final S3Service s3Service;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public JoinService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, S3Service s3Service){
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.s3Service = s3Service;
    }

    public ResultDTO<MyInfoResponseDTO> joinProcess(JoinDTO joinDTO) {

        //private AmazonS3 amazonS3;
        String nickname = joinDTO.getNickname();
        String password = joinDTO.getPassword();
        String email = joinDTO.getEmail();
        MultipartFile image = joinDTO.getImage();
        Boolean isExistNickname = userRepository.existsByNickname(nickname);
        Boolean isExistEmail = userRepository.existsByEmail(email);
        if(isExistNickname){
            return new ResultDTO<>("error", "이미 사용중인 닉네임입니다");
        }
        if(isExistEmail){
            return new ResultDTO<>("error", "이미 가입된 이메일주소입니다");
        }



        try{
            User data = new User();
            String default_image =  "https://ibb.co/4Ygw7TF";

            if(image != null && !image.isEmpty()){
                String profileImages = s3Service.upload(image, "profileImages");
                data.setProfile_image(profileImages);
            } else {
                data.setProfile_image(default_image);
            }

            data.setNickname(nickname);
            data.setEmail(email);
            data.setPassword(bCryptPasswordEncoder.encode(password));
            //data.setRole("ROLE_ADMIN");

            userRepository.save(data);
            MyInfoResponseDTO myInfoResponseDTO = convertToMyInfoResponseDTO(data);
            System.out.println(myInfoResponseDTO);
            return new ResultDTO<>("success", myInfoResponseDTO);
        }
        catch (IOException e) {
            ResultDTO<MyInfoResponseDTO> resultDTO =  new ResultDTO<>("error", "이미지 업로드 실패");
            return resultDTO;
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




