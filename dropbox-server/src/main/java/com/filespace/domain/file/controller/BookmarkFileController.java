package com.filespace.domain.file.controller;

import com.filespace.domain.file.dto.BookmarkFileRequest;
import com.filespace.domain.file.dto.BookmarkFileResponse;
import com.filespace.domain.file.service.BookmarkFileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/files/bookmark")
@RequiredArgsConstructor
@Slf4j
public class BookmarkFileController {

    private final BookmarkFileService bookmarkFileService;

    @Operation(summary = "Manage bookmark for a file", description = "Bookmark or unbookmark a file for a user")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = BookmarkFileRequest.class)))
                    ),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "404", description = "No Space existing"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    @PutMapping("/{fileId}")
    public ResponseEntity<String> manageBookmark(@PathVariable("fileId") Long fileId, @RequestParam("userId") Long userId) {
        bookmarkFileService.bookmarkFile(userId, fileId);
        return ResponseEntity.ok("Bookmark successfully.");
    }

    @Operation(summary = "Manage bookmark for a file", description = "Bookmark or unbookmark a file for a user")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = BookmarkFileRequest.class)))
                    ),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "404", description = "No Space existing"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    @GetMapping("/{userId}")
    public ResponseEntity<List<BookmarkFileResponse>> getBookmarkFilesByUserId(@PathVariable("userId") Long userId) {
        List<BookmarkFileResponse> bookmarkFiles = bookmarkFileService.getBookmarkFilesByUserId(userId);
        return ResponseEntity.ok(bookmarkFiles);
    }
}
