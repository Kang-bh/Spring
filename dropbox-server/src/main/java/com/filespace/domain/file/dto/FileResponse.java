package com.filespace.domain.file.dto;

import com.filespace.domain.file.domain.File;
import com.filespace.domain.folder.domain.Folder;
import com.filespace.domain.space.domain.Space;
import com.filespace.domain.user.domain.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class FileResponse {

    private Long fileId;
    private String fileName;
    private String fileExtension;
    private String filePath;
    private Long fileSize;
    private Space fileSpaceId;
    private Folder fileFolderId;
    private User fileUserId;
    private LocalDateTime fileCreatedAt;
    private LocalDateTime fileUpdatedAt;
    private LocalDateTime fileDeletedAt;
    private boolean isBookmarked;

    // Dto to Entity
    public static FileResponse of(File file, boolean isBookmarked) {
        return FileResponse.builder()
                .fileId(file.getFileId())
                .fileName(file.getFileName())
                .fileExtension(file.getFileExtension())
                .fileCreatedAt(file.getFileCreatedAt())
                .fileUpdatedAt(file.getFileCreatedAt())
                .fileDeletedAt(file.getFileDeletedAt())
                .filePath(file.getFilePath())
                .fileSize(file.getFileSize())
                .fileSpaceId(file.getFileSpaceId())
                .fileFolderId(file.getFileFolderId())
                .fileUserId(file.getFileUserId())
                .isBookmarked(isBookmarked).build();
    }
}
