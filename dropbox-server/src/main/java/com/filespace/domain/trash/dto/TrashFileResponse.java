package com.filespace.domain.trash.dto;

import com.filespace.domain.folder.domain.Folder;
import com.filespace.domain.space.domain.Space;
import com.filespace.domain.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class TrashFileResponse {

    private Long fileId;
    private String fileName;
    private String fileExtension;
    private LocalDateTime fileDeletedAt;
    private Long fileSpaceId;
    private Long fileFolderId;
    private Long fileUserId;
}
