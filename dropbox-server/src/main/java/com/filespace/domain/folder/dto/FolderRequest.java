package com.filespace.domain.folder.dto;

import com.filespace.domain.folder.domain.Folder;
import lombok.Getter;

@Getter
public class FolderRequest {

    private String name;
    private Long ownerId;
    private Long spaceId;
    private Long folderParentId;

    // Dto -> Entity
    public Folder toEntity() {
        return Folder.builder()
                .name(name)
                .folderUserId(ownerId)
                .folderSpaceId(spaceId)
                .folderParentId(folderParentId).build();
    }
}
