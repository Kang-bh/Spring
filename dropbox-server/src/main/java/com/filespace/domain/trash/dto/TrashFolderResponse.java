package com.filespace.domain.trash.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class TrashFolderResponse {

    private Long folderId;
    private String folderName;
    private LocalDateTime folderDeletedAt;
    private Long folderParentId;
    private Long folderUserId;
    private Long folderSpaceId;

}
