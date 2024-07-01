package com.filespace.domain.folder.controller;

import com.filespace.domain.folder.domain.Folder;
import com.filespace.domain.folder.dto.FolderRequest;
import com.filespace.domain.folder.dto.FolderResponse;
import com.filespace.domain.folder.service.FolderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.nio.file.AccessDeniedException;


@RestController
@RequestMapping("/api/folders")
@Tag(name = "Folder", description = "Folder API")
@RequiredArgsConstructor
@Slf4j
public class FolderController {

    private final FolderService folderService;

    @Operation(summary = "폴더를 조회합니다.")
    @ApiResponses (
        value = { @ApiResponse(responseCode = "200", description = "success"),
            @ApiResponse(responseCode = "400", description = "Bad Requests")}
    )
    @GetMapping(value = "/{folderId}")
    public ResponseEntity<FolderResponse> getFolderInfo(@PathVariable("folderId") Long folderId, @RequestParam("userId") Long userId) {

        // 폴더 정보 호출
        FolderResponse folderResponse = folderService.getFolderInfo(folderId, userId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(folderResponse);
    }

    @Operation(summary = "폴더를 생성합니다.")
    @ApiResponses (
            value = { @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "400", description = "Bad Requests")}
    )
    @PostMapping(value = "")
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<String> createFolder(@RequestBody FolderRequest folderRequest) {
        Folder newFolder = folderService.createFolder(folderRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(newFolder.getFolderName() + "가 성공적으로 생성되었습니다.");
    }

    @Operation(summary = "폴더명을 수정합니다.")
    @ApiResponses (
            value = { @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "400", description = "Bad Requests"),
                    @ApiResponse(responseCode = "403", description = "Forbidden - No Permission")}
    )
    @PutMapping(value = "/rename/{folderId}")
    public ResponseEntity<String> renameFolder(@PathVariable("folderId") Long folderId,
                                               @RequestParam("newName") String newName,
                                               @RequestParam("userId") Long userId) throws AccessDeniedException {
        Folder updateFolder = folderService.renameFolder(folderId, newName, userId);

        return ResponseEntity.status(HttpStatus.OK)
                .body("폴더명이 " + updateFolder.getFolderName() + "으로 수정되었습니다.");
    }

    @Operation(summary = "폴더 경로를 수정합니다.")
    @ApiResponses (
            value = { @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "400", description = "Bad Requests"),
                    @ApiResponse(responseCode = "403", description = "Forbidden - No Permission")}
    )
    @PutMapping(value = "/move/{folderId}")
    // required = false : 최상위 폴더일 경우, folderIdToMove = null
    public ResponseEntity<String> moveFolder(@PathVariable("folderId") Long folderId,
                                             @RequestParam(value = "folderIdToMove", required = false) Long folderIdToMove,
                                             @RequestParam("userId") Long userId) throws AccessDeniedException {
        Folder updateFolder = folderService.moveFolder(folderId, folderIdToMove, userId);

        return ResponseEntity.status(HttpStatus.OK)
                .body("폴더가 성공적으로 이동되었습니다.");
    }

    @Operation(summary = "공용 스페이스의 폴더를 삭제합니다.",
            description = "폴더는 바로 영구 삭제되며, 해당 folder 내 하위 폴더 및 파일들 또한 함께 영구 삭제됩니다.")
    @ApiResponses (
            value = { @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "400", description = "Bad Requests"),
                    @ApiResponse(responseCode = "403", description = "Forbidden - No Permission")}
    )
    @DeleteMapping(value = "/{folderId}")
    public ResponseEntity<String> deleteFolderInPublic(@PathVariable("folderId") Long folderId,
                                                       @RequestParam("userId") Long userId) throws Exception {
        folderService.deleteFolder(folderId, userId, true);

        return ResponseEntity.status(HttpStatus.OK)
                .body("폴더가 삭제되었습니다.");
    }

    @Operation(summary = "개인 스페이스의 폴더를 삭제합니다.")
    @ApiResponses (
            value = { @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "400", description = "Bad Requests")}
    )
    @PostMapping(value = "/{folderId}")
    public ResponseEntity<String> deleteFolderInPrivate(@PathVariable("folderId") Long folderId) throws Exception {
        folderService.trashFolder(folderId, true);

        return ResponseEntity.status(HttpStatus.OK)
                .body("폴더가 휴지통으로 이동되었습니다.");
    }

}
