package com.filespace.domain.trash.service;

import com.filespace.domain.file.domain.File;
import com.filespace.domain.file.dto.FileResponse;
import com.filespace.domain.file.repository.FileRepository;
import com.filespace.domain.file.service.FileService;
import com.filespace.domain.folder.domain.Folder;
import com.filespace.domain.folder.repository.FolderRepository;
import com.filespace.domain.trash.dto.TrashFileResponse;
import com.filespace.domain.trash.dto.TrashFolderResponse;
import com.filespace.domain.trash.dto.TrashResponse;
import com.filespace.domain.user.domain.User;
import com.filespace.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrashService {

    private final UserRepository userRepository;
    private final FileRepository fileRepository;
    private final FolderRepository folderRepository;
    private final FileService fileService;

    @Transactional
    public TrashResponse getTrash(Long userId) {

        Optional<User> user = userRepository.findById(userId);
        List<Folder> deletedFolders = folderRepository.findAllByUserIdAndTrashIsTrue(userId);
        List<File> deletedFiles = fileRepository.findAllByFileUserIdAndFileDeletedAtIsNotNull(user);

        List<TrashFolderResponse> trashFolders = deletedFolders.stream()
                .map(folder -> TrashFolderResponse.builder()
                        .folderId(folder.getFolderId())
                        .folderName(folder.getFolderName())
                        .folderDeletedAt(folder.getFolderDeletedAt())
                        .folderParentId(folder.getFolderParentId())
                        .folderUserId(folder.getUserId())
                        .folderSpaceId(folder.getSpaceId())
                        .build())
                .toList();

        // file 의 경우, 다음 두 가지에 대해 filtering 하는 작업 필요
        //    - 파일이 속한 폴더가 deleted 상태가 아닌 경우만 호출
        //    - 파일이 폴더 없이 바로 스페이스에 위치한 경우 호출
        List<TrashFileResponse> trashFiles = deletedFiles.stream()
                .filter(file -> {
                    if(file.getFileFolderId() == null)
                        return true;
                    else {
                        return file.getFileFolderId().getFolderDeletedAt() == null;
                    }
                })
                .map(file -> TrashFileResponse.builder()
                        .fileId(file.getFileId())
                        .fileName(file.getFileName())
                        .fileExtension(file.getFileExtension())
                        .fileDeletedAt(file.getFileDeletedAt())
                        .fileSpaceId(file.getFileSpaceId().getSpaceId())
                        .fileUserId(file.getFileUserId().getId())
                        .fileFolderId(file.getFileFolderId() != null ? file.getFileFolderId().getFolderId() : null)
                        .build())
                .toList();

        return TrashResponse.builder()
                .folders(trashFolders)
                .files(trashFiles).build();
    }

    @Transactional
    public void deleteFile(Long fileId) throws Exception {
        fileService.deleteFile(fileId);
    }

    @Transactional
    public void deleteFolder(Long folderId) throws Exception {
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new IllegalArgumentException("not found : folderId { " + folderId + " }"));

        // 1. Delete sub-folders recursively
        //  - deleted 된 folder 호출
        List<Folder> subFolders = folderRepository.findByFolderParentIdAndFolderDeletedAtIsNotNull(folderId);
        for (Folder subFolder : subFolders) {
            deleteFolder(subFolder.getFolderId());
        }

        // 2. Delete files in the current folder
        List<FileResponse> files = fileService.findAllDeletedFilesInFolder(folder.getFolderId());
        for(FileResponse file: files) {
            log.info("delete fileId : " + file.getFileId());
            fileService.deleteFile(file.getFileId());
        }

        // 3. Delete current folder
        folderRepository.delete(folder);
    }

    @Transactional
    public void restoreFile(Long fileId) {
        File file = fileRepository.findByFileId(fileId)
                .orElseThrow(() -> new IllegalArgumentException("not found : folderId { " + fileId + " }"));

        file.setFileDeletedAt(null);

    }

    @Transactional
    public void restoreFolder(Long folderId, boolean restore) {
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new IllegalArgumentException("not found : folderId { " + folderId + " }"));

        // 복구하고자하는 폴더의 trash 값만 false 로 변경
        if (restore == true) {
            folder.setTrash(false);
        }

        // 1. Update folder_Deleted_At column recursively -> null
        List<Folder> subFolders = folderRepository.findByFolderParentIdAndFolderDeletedAtIsNotNull(folderId);
        for (Folder subFolder : subFolders) {
            restoreFolder(subFolder.getFolderId(), false);
        }

        // 2. Update file_Deleted_At column recursively -> null
        List<FileResponse> files = fileService.findAllDeletedFilesInFolder(folder.getFolderId());
        for(FileResponse file: files) {
            restoreFile(file.getFileId());
        }

        // 3. Update folder_Deleted_At current folder
        folder.setFolderDeletedAt(null);

    }

    // 휴지통에 있는 폴더 중 삭제일로부터 30일 지난 폴더, 자동 영구삭제
    // 매일 0시 0분 0초, 스케줄러 실행됨
    @Scheduled(cron = "0 0 0 * * *")
    public void autoDeleteFolder() throws Exception {

        log.info(LocalDateTime.now() + " autoDelete scheduler 실행");

        List<Folder> deletedFolders = folderRepository.findAllByTrashIsTrue();
        List<Long> autoDeletedFolderIds = new ArrayList<>();
        List<Long> autoDeletedFileIds = new ArrayList<>();

        for (Folder folder : deletedFolders) {
            LocalDateTime deletedAt = folder.getFolderDeletedAt();
            if (deletedAt != null) {
                Duration duration = Duration.between(deletedAt, LocalDateTime.now());

                if (duration.toDays() >= 30) {     // 자동 영구삭제 기한, 30일 이상
                    autoDeletedFolderIds.add(folder.getFolderId());

                    Optional<List<File>> filesInFolder = fileRepository.findAllByFileFolderIdAndFileDeletedAtIsNotNull(folder);
                    if (filesInFolder.isPresent()) {
                        List<File> files = filesInFolder.get();

                        if (files != null && !files.isEmpty()) {
                            for (File file : files) {
                                autoDeletedFileIds.add(file.getFileId());
                            }
                        }
                    }
                }
            } else {
                log.warn("해당 폴더 삭제 일시가 설정되어있지 않습니다. folderId : " + folder.getFolderId());
            }

        }

        for (Long fileId : autoDeletedFileIds) {
            deleteFile(fileId);
        }

        for (Long folderId : autoDeletedFolderIds) {
            deleteFolder(folderId);
        }

    }

    // 휴지통에 있는 파일 중 삭제일로부터 30일 지난 파일, 자동 영구삭제
    // 매일 0시 0분 0초, 스케줄러 실행됨
    @Scheduled(cron = "0 0 0 * * *")
    public void autoDeleteFile() throws Exception {

        log.info(LocalDateTime.now() + " autoDelete scheduler 실행");

        List<File> deletedFiles = fileRepository.findAllByFileDeletedAtIsNotNull();
        List<Long> autoDeletedFileIds = new ArrayList<>();

        for (File file : deletedFiles) {
            LocalDateTime deletedAt = file.getFileDeletedAt();
            if (deletedAt != null) {
                Duration duration = Duration.between(deletedAt, LocalDateTime.now());

                if (duration.toDays() >= 30) {     // 자동 영구삭제 기한, 30일 이상
                    autoDeletedFileIds.add(file.getFileId());
                }
            }
        }

        for (Long fileId : autoDeletedFileIds) {
            deleteFile(fileId);
        }

    }
}