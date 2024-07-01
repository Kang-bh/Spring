package com.filespace.domain.user.controller;

import com.filespace.domain.user.dto.EditDTO;
import com.filespace.domain.user.dto.JoinDTO;
import com.filespace.domain.user.dto.MyInfoResponseDTO;
import com.filespace.domain.user.dto.ResultDTO;
import com.filespace.domain.user.service.EditMyInfoService;
import com.filespace.domain.user.service.JoinService;
import com.filespace.domain.user.service.MyInfoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@ResponseBody
public class MyInfoController {

    private final MyInfoService myInfoService;
    private final EditMyInfoService editMyInfoService;

    public MyInfoController(MyInfoService myInfoService, EditMyInfoService editMyInfoService) {
        this.myInfoService = myInfoService;
        this.editMyInfoService = editMyInfoService;
    }

    @GetMapping(value = "/MyInfo")
    public ResponseEntity<ResultDTO<MyInfoResponseDTO>> getmyinfo() {
        MyInfoResponseDTO myInfoResponseDTO;
        ResultDTO<MyInfoResponseDTO> resultDTO = myInfoService.getMyInfo();
        return ResponseEntity.status(HttpStatus.OK).body(resultDTO);
    }

    @PostMapping(value = "/MyInfo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResultDTO<MyInfoResponseDTO>> postMyinfo(EditDTO editDTO, @RequestPart(value = "profile_image", required = false) MultipartFile image) {
        MyInfoResponseDTO myInfoResponseDTO;
        ResultDTO<MyInfoResponseDTO> resultDTO = editMyInfoService.editMyInfo(editDTO, image);

        if(resultDTO.getStatus().equals("success")) {
            return ResponseEntity.status(HttpStatus.OK).body(resultDTO);//"회원정보 수정 성공"
        }
        else {
            if(resultDTO.getMessage().equals("이미지 업로드 실패")) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resultDTO);//이미지 업로드 실패
            }
            return ResponseEntity.status(HttpStatus.CONFLICT).body(resultDTO);//
        }

    }
}
