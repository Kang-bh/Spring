package com.filespace.domain.file.service;

import com.filespace.domain.file.domain.File;
import com.filespace.domain.file.dto.FileResponse;
import com.filespace.domain.space.domain.Space;
import com.filespace.domain.space.dto.LogRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@Service
public interface FileService {

    // 1. 파일 업로드
    String uploadFile(MultipartFile file, Long spaceId, Long folderId, Long userId) throws IOException;

    // 2. 파일 목록 조회
    List<FileResponse> findAllFilesInFolder(Long folderId, Long userId);

    List<FileResponse> findAllDeletedFilesInFolder(Long folderId);

    // 3. 파일 자세히 보기
    FileResponse getFileById(Long fileId);

    // 4. 파일 수정 (이름 또는 경로)
    FileResponse updateFile(Long fileId, String fileName, Long folderId, String what) throws FileNotFoundException;

    // 5. 파일 영구 삭제
    void deleteFile(Long fileId) throws Exception;

    // 6. 파일 일시적 삭제
    void trashFile(Long fileId) throws Exception;

    List<FileResponse> findAllRootFiles(Long SpaceId, Long UserId);

    List<FileResponse> findAllFilesBySpaceId(Long spaceId);

    void createDownloadLog(Long fileId, LogRequest.createLog data);
}
