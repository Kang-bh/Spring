package com.filespace.domain.user.service;

import com.filespace.domain.user.dto.EditDTO;
import com.filespace.domain.user.dto.MyInfoResponseDTO;
import com.filespace.domain.user.dto.ResultDTO;
import com.filespace.domain.user.domain.User;
import com.filespace.domain.user.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class EditMyInfoService {
    private final UserRepository userRepository;
    private final S3Service s3Service;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public EditMyInfoService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, S3Service s3Service){
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.s3Service = s3Service;
    }

    public ResultDTO<MyInfoResponseDTO> editMyInfo(EditDTO editDTO, MultipartFile image) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println(email);
        User data = userRepository.findByEmail(email);

        String orinalnickname = data.getNickname();
        String nickname = editDTO.getNickname();

        Boolean isExist = userRepository.existsByNickname(nickname);


        if (! nickname.equals(orinalnickname)) {
            if (isExist) {
                return new ResultDTO<>("error", "이미 사용중인 닉네임입니다");
            }
            else {
                try{
                    if (image != null && !image.isEmpty()) {
                        String profileImages = s3Service.upload(image, "profileImages");
                        data.setProfile_image(profileImages);
                    } else {
                    }
                }
                catch(IOException e) {
                    ResultDTO<MyInfoResponseDTO> resultDTO =  new ResultDTO<>("error", "이미지 업로드 실패");
                    return resultDTO;
                }
            }

        } else {

            try{
                if (image != null && !image.isEmpty()) {
                    String profileImages = s3Service.upload(image, "profileImages");
                    data.setProfile_image(profileImages);
                }
            }
            catch(IOException e) {
                ResultDTO<MyInfoResponseDTO> resultDTO =  new ResultDTO<>("error", "이미지 업로드 실패");
                return resultDTO;
            }

        }
        data.setNickname(nickname);
        userRepository.save(data);
        MyInfoResponseDTO myInfoResponseDTO = convertToMyInfoResponseDTO(data);
        System.out.println(myInfoResponseDTO);
        return new ResultDTO<>("success", myInfoResponseDTO);
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
