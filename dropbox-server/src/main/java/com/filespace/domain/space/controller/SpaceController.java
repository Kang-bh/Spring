package com.filespace.domain.space.controller;

import com.filespace.domain.file.dto.FileResponse;
import com.filespace.domain.file.service.FileService;
import com.filespace.domain.folder.dto.FolderResponse;
import com.filespace.domain.folder.service.FolderService;
import com.filespace.domain.space.domain.Space;
import com.filespace.domain.space.dto.AddSpaceRequest;
import com.filespace.domain.space.dto.SpaceResponse;
import com.filespace.domain.space.dto.UpdateSpaceRequest;
import com.filespace.domain.space.service.SpaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.expression.AccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@Tag(name = "Space", description = "Space API")
@Validated
public class SpaceController {
    private final SpaceService spaceService;
    private final FileService fileService;
    private final FolderService folderService;

    @Operation(summary = "get space list", description = "use this when get own space list or search space list")
    @ApiResponses(
            value = {
                 @ApiResponse(responseCode = "200", description = "success",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Space.class)))
                 ),
                @ApiResponse(responseCode = "400", description = "Bad Requests",
                        content = @Content(
                                mediaType = "application/json"
                        )
                ),
                @ApiResponse(responseCode = "404", description = "No Space existing",
                        content = @Content(
                                mediaType = "application/json"
                        )
                )
            })
    @GetMapping("/api/spaces")
    // todo : user 생성 후 수정 필요
    // todo : check response dto
    public ResponseEntity<List<SpaceResponse.SpaceInfo>> findAllSpaces (
            @RequestParam(name = "isParticipated", required = false, defaultValue = "false") Boolean isParticipated,
            @RequestParam(name = "userId") long userId
    ) {
        List<SpaceResponse.SpaceInfo> spaceList = spaceService.findAllSpace(userId, isParticipated);

        return ResponseEntity.status(HttpStatus.OK)
                .body(spaceList);
    }

    @Operation(summary = "get space by spaceId", description = "use this when get space by Id")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success",
                            content = @Content(
                                    schema = @Schema(implementation = Space.class),
                                    mediaType = "application/json"
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Bad Requests",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "No Space existing",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            })
    @GetMapping("/api/spaces/{spaceId}")
    public ResponseEntity<SpaceResponse.SpaceInfo> findSpace(@PathVariable("spaceId") long spaceId) {
        SpaceResponse.SpaceInfo space = spaceService.findSpaceById(spaceId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(space);
    }

    @Operation(summary = "create space", description = "use this when create space")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "success"),
                    @ApiResponse(responseCode = "400", description = "Bad Requests"),
                    @ApiResponse(responseCode = "404", description = "No Space existing")
            })
    @PostMapping("/api/spaces")
    // todo : security 완료시 principal 추가
    public ResponseEntity<Void> addSpace(
            @RequestBody AddSpaceRequest spaceRequest,
            @RequestParam(name = "userId") long userId
    ) {

        // todo : user 추가
        // todo : check public or private if private -> check password
        // todo : s3 bucket create

        Space createdSpace = spaceService.save(spaceRequest, userId);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "delete space by spaceId", description = "use this when delete your space")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "success"),
                    @ApiResponse(responseCode = "400", description = "Bad Requests"),
                    @ApiResponse(responseCode = "404", description = "No Space existing")
            })
    @DeleteMapping("/api/spaces/{spaceId}")
    public ResponseEntity<Void> deleteSpace(
            @PathVariable("spaceId") long spaceId,
            @RequestParam(name = "userId") long userId
    ) throws AccessException {
        spaceService.deleteSpaceById(spaceId, userId); // todo :userId

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }

    @Operation(summary = "modify space by spaceId", description = "use this when modify space")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = Space.class)))
                    ),
                    @ApiResponse(responseCode = "400", description = "Bad Requests"),
                    @ApiResponse(responseCode = "404", description = "No Space existing")
            })
    @PutMapping("/api/spaces/{spaceId}")
    public ResponseEntity<SpaceResponse.SpaceInfo> updateSpace(
            @PathVariable("spaceId") long spaceId,
            @RequestParam(name = "userId") long userId,
            @RequestBody UpdateSpaceRequest spaceRequest
    ) throws AccessException {
        SpaceResponse.SpaceInfo space = spaceService.updateSpace(spaceId, spaceRequest, userId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(space);
    }

    @GetMapping("/api/spaces/{spaceId}/root")
    public ResponseEntity<?> rootSpaceInfo(
            @PathVariable("spaceId") Long spaceId,
            @RequestParam(name = "userId") Long userId
    ) {
        List<FileResponse> rootFiles = fileService.findAllRootFiles(spaceId, userId);
        List<FolderResponse> rootFolders = folderService.getRootFolders(spaceId);

        Map<String, Object> response = new HashMap<>();
        response.put("rootFolders", rootFolders);
        response.put("rootFiles", rootFiles);

        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }
    // 변경
//    @PostMapping("/api/spaces/{spaceId}/enter")
//    public ResponseEntity<Void> enterSpace(
//            @PathVariable("spaceId") long spaceId,
//            @RequestParam(name = "userId", required = false) Long userId
//    ) {
//
//
//        // todo : data 꼴 합의
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .build();
//
//    }

//    @DeleteMapping("/api/spaces/{spaceId}/exit")
//    public ResponseEntity<Void> exitSpace(
//            @PathVariable("spaceId") long spaceId,
//            @RequestParam(name = "userId", required = false) Long userId
//    ) {
//        // owner 면 space 삭제
//        // todo: socket or redis or kafka 통해서 삭제된 것 알림
//
//    }


}
