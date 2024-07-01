package com.filespace.domain.dashboard.service;

import com.filespace.domain.dashboard.dto.DashBoardResponse;
import com.filespace.domain.file.domain.File;
import com.filespace.domain.file.dto.FileResponse;
import com.filespace.domain.space.domain.Space;
import com.filespace.domain.space.dto.LogResponse;
import com.filespace.domain.space.dto.SpaceResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface DashBoardService {

    // storage 현황 조회 API
    public DashBoardResponse.spaceStorageInfo getSpaceStorageUsage (List<FileResponse> files);
    public List<LogResponse.LogInfo> getLogs (Long userId);

}
