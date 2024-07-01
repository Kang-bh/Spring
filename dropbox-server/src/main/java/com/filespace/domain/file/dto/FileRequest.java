package com.filespace.domain.file.dto;

import com.filespace.domain.file.domain.File;
import com.filespace.domain.folder.domain.Folder;
import com.filespace.domain.space.domain.Space;
import com.filespace.domain.user.domain.User;
import lombok.Getter;

@Getter
public class FileRequest {

    private String fileName;
    private Space fileSpaceId;
    private Folder fileFolderId;
    private User fileUserId;

    // Dto to Entity
    public File toEntity() {
        return File.builder()
                .fileName(fileName)
                .fileSpaceId(fileSpaceId)
                .fileFolderId(fileFolderId)
                .fileUserId(fileUserId).build();
    }
}
