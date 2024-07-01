package com.filespace.domain.folder.service;

import com.filespace.domain.file.dto.FileResponse;
import com.filespace.domain.file.service.FileService;
import com.filespace.domain.folder.domain.Folder;
import com.filespace.domain.folder.dto.FolderRequest;
import com.filespace.domain.folder.dto.FolderResponse;
import com.filespace.domain.folder.repository.FolderRepository;
import com.filespace.domain.space.domain.Space;
import com.filespace.domain.space.repository.SpaceRepository;
import com.filespace.domain.user.domain.User;
import com.filespace.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FolderService {

    private final UserRepository userRepository;
    private final SpaceRepository spaceRepository;
    private final FolderRepository folderRepository;
    private final FileService fileService;


    @Transactional
    public FolderResponse getFolderInfo(Long folderId, Long userId) {

        // 1. 폴더 존재 여부 확인
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new IllegalArgumentException("not found : folderId { " + folderId + " }"));

        // 2. 폴더 정보 FolderResponse Dto에 담아 반환
        //    - 해당 폴더 정보
        //    - 하위 폴더 및 파일
        if (folder.getFolderDeletedAt() == null) {
            List<Folder> subFolders = folderRepository.findByFolderParentIdAndFolderDeletedAtIsNull(folderId);
            List<FileResponse> files = fileService.findAllFilesInFolder(folder.getFolderId(), userId);
            return FolderResponse.of(folder, mapToFolderResponseList(subFolders), files);
        } else {
            // 예외처리 ) 해당 folderId를 가진 폴더가 삭제되어 휴지통에 있는 경우
            throw new IllegalArgumentException(
                    "already deleted folder : you can check folderId { " + folderId + " } in the trash directory.");
        }
    }

    private List<FolderResponse> mapToFolderResponseList(List<Folder> folders) {
        List<FolderResponse> folderResponses = new ArrayList<>();
        for (Folder folder : folders) {
            folderResponses.add(FolderResponse.of(folder, null, null));
        }
        return folderResponses;
    }

    @Transactional
    public Folder createFolder(FolderRequest folderRequest) {

        // 1. 사용자 확인
        User user = userRepository.findById(folderRequest.getOwnerId())
                .orElseThrow(() -> new IllegalArgumentException("not found : userId { " + folderRequest.getOwnerId() + " }"));

        // 2. 스페이스 확인
        Space space = spaceRepository.findById(folderRequest.getSpaceId())
                .orElseThrow(() -> new IllegalArgumentException("not found : spaceId { " + folderRequest.getSpaceId() + " }"));

        // 3. 폴더 생성
        Folder folder = folderRequest.toEntity();
        return folderRepository.save(folder);
    }

    @Transactional
    public Folder renameFolder(Long folderId, String newName, Long userId) throws AccessDeniedException {
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new IllegalArgumentException("not found : folderId { " + folderId + " }"));

        // 수정 권한 확인
        if(userId != folder.getUserId()){
            throw new AccessDeniedException("폴더를 수정할 권한이 없습니다.");
        }

        folder.rename(newName);

        return folder;
    }

    @Transactional
    public Folder moveFolder(Long folderId, Long folderParentId, Long userId) throws AccessDeniedException {
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new IllegalArgumentException("not found : folderId { " + folderId + " }"));

        // 1. 수정 권한 확인
        // - folder 생성자와 수정 요구자 일치하지 않을 시, exception 발생
        if(userId != folder.getUserId()) {
            throw new AccessDeniedException("폴더를 수정할 권한이 없습니다.");
        }

        // 2. 폴더 이동 가능여부 판단
        // - folder, 다른 space 로 이동 불가
        // - 현재 폴더가 위치한 space 아닌 다른 space 로의 폴더 이동요청 들어올 시, exception 발생
        if (folderParentId != null) {
            if (folder.getSpaceId() != folderRepository.findById(folderParentId).get().getSpaceId()) {
                throw new IllegalArgumentException("해당 폴더로 이동할 수 없습니다 : 다른 space로의 이동 불가");
            }
        }

        folder.modifyPath(folderParentId);

        return folder;
    }

    @Transactional
    public void deleteFolder(Long folderId, Long userId, boolean check) throws Exception {
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new IllegalArgumentException("not found : folderId { " + folderId + " }"));
        Space space = spaceRepository.findById(folder.getSpaceId()).
                orElseThrow(() -> new IllegalArgumentException("not found : spaceId { " + folder.getSpaceId() + " }"));

        // 1. 최초에만 폴더 삭제 권한 확인
        if(check) {
            if(userId != folder.getUserId() && userId != space.getOwnerId().getId())
                throw new AccessDeniedException("폴더를 수정할 권한이 없습니다.");
        }

        // 2. 폴더 삭제
        // 1) Delete sub-folders recursively
        List<Folder> subFolders = folderRepository.findByFolderParentIdAndFolderDeletedAtIsNull(folderId);
        for (Folder subFolder : subFolders) {
            log.info(subFolder.getFolderId() + " delete 진입");
            deleteFolder(subFolder.getFolderId(), userId, false);
        }

        // 2) Delete files in the current folder
        List<FileResponse> files = fileService.findAllFilesInFolder(folder.getFolderId(), userId);
        for(FileResponse file: files) {
            log.info("fileId " + file.getFileId() + " delete 진입");
            fileService.deleteFile(file.getFileId());
        }

        // 3) Delete current folder
        folderRepository.delete(folder);

    }

    @Transactional
    public void trashFolder(Long folderId, boolean trash) throws Exception {
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new IllegalArgumentException("not found : folderId { " + folderId + " }"));

        // 삭제하고자 하는 폴더의 trash 값만 true 로 변경
        if (trash == true) {
            folder.setTrash(true);
        }

        // 1. Update folder_Deleted_At column recursively
        List<Folder> subFolders = folderRepository.findByFolderParentIdAndFolderDeletedAtIsNull(folderId);
        log.info(subFolders.toString());
        for (Folder subFolder : subFolders) {
            trashFolder(subFolder.getFolderId(), false);
        }

        // 2. Update file_Deleted_At column recursively
        List<FileResponse> files = fileService.findAllFilesInFolder(folder.getFolderId(), folder.getUserId());
        for(FileResponse file: files) {
            fileService.trashFile(file.getFileId());
        }

        // 3. Update folder_Deleted_At current folder
        folder.setFolderDeletedAt(LocalDateTime.now());

    }

    public List<FolderResponse> getRootFolders(Long spaceId) {
        System.out.println("rootFolders : " + spaceId);
        List<Folder> rootFolders = folderRepository.findAllByFolderParentIdIsNullAndFolderDeletedAtIsNullAndSpaceId(spaceId);

        System.out.println("rootFolders : " + rootFolders);

        return mapToFolderResponseList(rootFolders);

    }
}