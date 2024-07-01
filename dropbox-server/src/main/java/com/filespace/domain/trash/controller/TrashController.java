package com.filespace.domain.trash.controller;

import com.filespace.domain.folder.repository.FolderRepository;
import com.filespace.domain.trash.dto.TrashResponse;
import com.filespace.domain.trash.service.TrashService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trash")
@Tag(name = "Trash", description = "Trash API")
@RequiredArgsConstructor
public class TrashController {

    private final TrashService trashService;

    @Operation(summary = "휴지통을 조회합니다.")
    @ApiResponses(
            value = { @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "400", description = "Bad Requests")}
    )
    @GetMapping("/{userId}")
    public ResponseEntity<TrashResponse> getTrash(@PathVariable("userId") Long userId) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(trashService.getTrash(userId));
    }

    @Operation(summary = "휴지통에 있는 파일을 영구 삭제합니다.")
    @ApiResponses(
            value = { @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "400", description = "Bad Requests")}
    )
    @DeleteMapping("/deleteFile/{fileId}")
    public ResponseEntity<String> deleteFile(@PathVariable("fileId") Long fileId) throws Exception {
        trashService.deleteFile(fileId);

        return ResponseEntity.status(HttpStatus.OK)
                .body("파일이 삭제되었습니다.");
    }

    @Operation(summary = "휴지통에 있는 폴더를 영구 삭제합니다.")
    @ApiResponses (
            value = { @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "400", description = "Bad Requests")}
    )
    @DeleteMapping("/deleteFolder/{folderId}")
    public ResponseEntity<String> deleteFolder(@PathVariable("folderId") Long folderId, @RequestParam("userId") Long userId) throws Exception {
        trashService.deleteFolder(folderId);

        return ResponseEntity.status(HttpStatus.OK)
                .body("폴더가 삭제되었습니다.");
    }

    @Operation(summary = "휴지통에 있는 파일을 복구합니다.")
    @ApiResponses (
            value = { @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "400", description = "Bad Requests")}
    )
    @PutMapping("/restoreFile/{fileId}")
    public ResponseEntity<String> restoreFile(@PathVariable("fileId") Long fileId) {
        trashService.restoreFile(fileId);

        return ResponseEntity.status(HttpStatus.OK)
                .body("파일이 복구되었습니다.");
    }

    @Operation(summary = "휴지통에 있는 폴더를 복구합니다.")
    @ApiResponses (
            value = { @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "400", description = "Bad Requests")}
    )
    @PutMapping("/restoreFolder/{folderId}")
    public ResponseEntity<String> restoreFolder(@PathVariable("folderId") Long folderId) {
        trashService.restoreFolder(folderId, true);

        return ResponseEntity.status(HttpStatus.OK)
                .body("폴더가 복구되었습니다.");
    }

    @Operation(summary = "삭제한 지 30일이 지난 폴더를 휴지통에서 영구 삭제합니다.")
    @ApiResponses (
            value = { @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "400", description = "Bad Requests")}
    )
    @DeleteMapping("/autoDelete")
    public ResponseEntity<Void> autoDelete() throws Exception {
        trashService.autoDeleteFolder();
        trashService.autoDeleteFile();

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

}
