package com.filespace.domain.dashboard.controller;


import com.filespace.domain.dashboard.dto.DashBoardResponse;
import com.filespace.domain.dashboard.service.DashBoardService;
import com.filespace.domain.file.dto.FileResponse;
import com.filespace.domain.file.service.FileService;
import com.filespace.domain.space.dto.LogResponse;
import com.filespace.domain.space.dto.SpaceResponse;
import com.filespace.domain.space.service.SpaceService;
import com.filespace.domain.user.domain.User;
import com.filespace.domain.user.service.UserIdService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.protocol.HTTP;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@ResponseBody
@RequestMapping("/api/dashboards")
@RequiredArgsConstructor
@Tag(name = "DashBoard", description = "DashBoard API")
@Slf4j
public class DashBoardController {

    private final DashBoardService dashBoardService;
    private final FileService fileService;
    private final SpaceService spaceService;
    private final UserIdService userIdService;



    // todo : fix swagger info
    @Operation(summary = "get storage usage", description = "본인 스토리지의 확장자별 용량을 확인합니다. \n 조회 시 Access Token을 담아서 요청해야합니다.")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "success",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DashBoardResponse.spaceStorageInfo.class)
                    )
                ),
                @ApiResponse(responseCode = "400", description = "Bad Requests",
                        content = @Content(
                                mediaType = "application/json"
                        )
                ),
                @ApiResponse(responseCode = "403", description = "Forbidden - No Permission",  content = @Content(
                        mediaType = "application/json"
                ))
            }
    )
    @GetMapping(value = "/storage-usage")
    public ResponseEntity<DashBoardResponse.spaceStorageInfo> getSpaceStorageUsage(
            @RequestParam("userId") Long userId
    ) {
        // in db or s3
//        Long userId = userIdService.getCurrentUserId();
        SpaceResponse.SpaceInfo spaceInfo = spaceService.getOwnSpace(userId);
        List<FileResponse> fileList = fileService.findAllFilesBySpaceId(spaceInfo.getSpaceId());
        DashBoardResponse.spaceStorageInfo result = dashBoardService.getSpaceStorageUsage(fileList);

        return ResponseEntity.status(HttpStatus.OK)
                .body(result);
    }

    @Operation(summary = "get log Info by user", description = "유저가 업로드 다운로드 한 파일들의 로그를 조회합니다. \n 조회 시 Access Token을 담아서 요청해야합니다.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = LogResponse.LogInfo.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Bad Requests",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Forbidden - No Permission",  content = @Content(
                            mediaType = "application/json"
                    ))
            }
    )
    @GetMapping("/logs")
    public ResponseEntity<List<LogResponse.LogInfo>> getLogs(
            @RequestParam("userId") Long userId
    ) {
//        Long userId = userIdService.getCurrentUserId();
        List<LogResponse.LogInfo> logInfoList = dashBoardService.getLogs(userId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(logInfoList);
    }

}
