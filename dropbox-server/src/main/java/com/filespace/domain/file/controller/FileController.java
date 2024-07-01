package com.filespace.domain.file.controller;

import com.filespace.domain.file.domain.File;
import com.filespace.domain.file.dto.FileResponse;
import com.filespace.domain.file.service.FileService;
import com.filespace.domain.space.dto.LogRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
@Slf4j
public class FileController {

    private final FileService fileService;

    @Operation(summary = "Get files list", description = "use this when get own file list or search file list")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = FileResponse.class)))
                    ),
                    @ApiResponse(responseCode = "400", description = "Bad Requests"),
                    @ApiResponse(responseCode = "404", description = "No Space existing")
            })
    @GetMapping("/folder/{folderId}")
    public ResponseEntity<List<FileResponse>> getFilesInFolder(@PathVariable("folderId") Long folderId, @PathVariable("userId") Long userId) {
        List<FileResponse> files = fileService.findAllFilesInFolder(folderId, userId);
        return ResponseEntity.ok().body(files);
    }

    @Operation(summary = "Upload a file", description = "Upload a file to a specific folder in a space")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = FileResponse.class)))
                    ),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "404", description = "No Space existing"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    @PostMapping()
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("spaceId") Long spaceId, @RequestParam(value = "folderId", required = false) Long folderId, @RequestParam("userId") Long userId) throws IOException {
        String fileUrl = fileService.uploadFile(file, spaceId, folderId, userId);
        return ResponseEntity.ok(fileUrl);
    }

    @Operation(summary = "Get a file by ID", description = "Search a file by its unique ID")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = FileResponse.class)))
                    ),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "404", description = "No Space existing"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    @GetMapping("/{fileId}")
    public ResponseEntity<FileResponse> getFileById(@PathVariable("fileId") Long fileId) {
        FileResponse file = fileService.getFileById(fileId);
        return ResponseEntity.ok(file);
    }

    @Operation(summary = "Update a file", description = "Update the details of a file by its ID")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = FileResponse.class)))
                    ),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "404", description = "No Space existing"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    @PutMapping("/{fileId}")
    public ResponseEntity<FileResponse> updateFile(@PathVariable("fileId") Long fileId,
                                                   @RequestParam(name = "what") String what,
                                                   @RequestParam(name = "fileName",required = false) String name,
                                                   @RequestParam(name = "folderId",required = false) Long folderId) {
        try {
            FileResponse updatedFile = fileService.updateFile(fileId, name, folderId, what);
            return ResponseEntity.ok(updatedFile);
        } catch (FileNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Delete a public file", description = "Delete a file by its ID")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = FileResponse.class)))
                    ),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "404", description = "No Space existing"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    @DeleteMapping("/{fileId}")
        public ResponseEntity<String> deleteFileInPublic(@PathVariable("fileId") Long fileId) {
            try {
                fileService.deleteFile(fileId);
                return ResponseEntity.ok("File deleted successfully");
            } catch (FileNotFoundException e) {
                return ResponseEntity.notFound().build();
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }

    @Operation(summary = "Delete a private file", description = "Delete a file by its ID")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = FileResponse.class)))
                    ),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "404", description = "No Space existing"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    @PostMapping("/{fileId}")
    public ResponseEntity<String> deleteFileInPrivate(@PathVariable("fileId") Long fileId) {
        try {
            fileService.trashFile(fileId);
            return ResponseEntity.ok("File deleted successfully");
        } catch (FileNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/{fileId}/download")
    public ResponseEntity<Void> createDownloadLog(
            @PathVariable("fileId") Long fileId,
            @RequestBody LogRequest.createLog data
        ) {
        try {
            fileService.createDownloadLog(fileId, data);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
