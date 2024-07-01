package com.filespace.domain.folder.dto;

import com.filespace.domain.folder.domain.Folder;
import com.filespace.domain.file.dto.FileResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class FolderResponse {

    private Long folderId;
    private String folderName;
    private Long folderSize;
    private boolean trash;
    private LocalDateTime folderCreatedAt;
    private LocalDateTime folderUpdatedAt;
    private LocalDateTime folderDeletedAt;
    private Long folderParentId;
    private Long folderUserId;
    private Long folderSpaceId;
    private List<FolderResponse> subFolders;
    private List<FileResponse> files;

    public static FolderResponse of(Folder folder, List<FolderResponse> subFolders, List<FileResponse> files) {
        return FolderResponse.builder()
                .folderId(folder.getFolderId())
                .folderName(folder.getFolderName())
                .folderSize(folder.getFolderSize())
                .trash(folder.isTrash())
                .folderCreatedAt(folder.getFolderCreatedAt())
                .folderUpdatedAt(folder.getFolderUpdatedAt())
                .folderDeletedAt(folder.getFolderDeletedAt())
                .folderParentId(folder.getFolderParentId())
                .folderUserId(folder.getUserId())
                .folderSpaceId(folder.getSpaceId())
                .subFolders(subFolders)
                .files(files)
                .build();
    }
}