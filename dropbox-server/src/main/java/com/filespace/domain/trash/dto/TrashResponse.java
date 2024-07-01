package com.filespace.domain.trash.dto;

import com.filespace.domain.file.dto.FileResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class TrashResponse {

    private List<TrashFolderResponse> folders;
    private List<TrashFileResponse> files;

}
