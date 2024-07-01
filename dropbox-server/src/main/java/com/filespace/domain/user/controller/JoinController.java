package com.filespace.domain.user.controller;

import com.filespace.domain.space.dto.AddSpaceRequest;
import com.filespace.domain.space.enumuration.SpaceType;
import com.filespace.domain.user.dto.JoinDTO;
import com.filespace.domain.user.dto.MyInfoResponseDTO;
import com.filespace.domain.user.dto.ResultDTO;
import com.filespace.domain.user.service.JoinService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.Bean;
import com.filespace.domain.space.service.SpaceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@ResponseBody
public class JoinController {

    private final JoinService joinService;
    private final SpaceService spaceService;

    public JoinController(JoinService joinService, SpaceService spaceService) {
        this.joinService = joinService;
        this.spaceService = spaceService;
    }


    @PostMapping(value = "/join", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResultDTO<MyInfoResponseDTO>> joinProcess(JoinDTO joinDTO) {

            //System.out.println("image : "+ image);
            ResultDTO<MyInfoResponseDTO> resultDTO = joinService.joinProcess(joinDTO);

            AddSpaceRequest spaceRequest = new AddSpaceRequest();

            System.out.println("resultDTO : "+ resultDTO);
            System.out.println("resultDTO : "+ resultDTO.getData());


            spaceRequest.setName(joinDTO.getEmail().split("@")[0]);
            spaceRequest.setType(SpaceType.PRIVATE);
            spaceRequest.setMaxPeople((long) 1);
            spaceRequest.setTotalStorage((long) 256);
            spaceRequest.setIsPublic(false);

            spaceService.save(spaceRequest, resultDTO.getData().getId());

            if(resultDTO.getStatus().equals("success")) {
                return ResponseEntity.status(HttpStatus.OK).body(resultDTO);//"회원가입 성공."
            }
            else {
                if(resultDTO.getMessage().equals("이미지 업로드 실패")) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resultDTO);
                }
                else {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body(resultDTO);
                }

            }


    }
}
