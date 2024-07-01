package com.filespace.domain.file.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.SourceSelectionCriteria;
import com.filespace.domain.file.domain.BookmarkFile;
import com.filespace.domain.file.domain.File;
import com.filespace.domain.file.dto.FileResponse;
import com.filespace.domain.file.repository.BookmarkFileRepository;
import com.filespace.domain.file.repository.FileRepository;
import com.filespace.domain.folder.domain.Folder;
import com.filespace.domain.folder.repository.FolderRepository;
import com.filespace.domain.space.domain.Log;
import com.filespace.domain.space.domain.Space;
import com.filespace.domain.space.dto.LogRequest;
import com.filespace.domain.space.enumuration.LogType;
import com.filespace.domain.space.enumuration.SpaceType;
import com.filespace.domain.space.repository.LogRepository;
import com.filespace.domain.space.repository.SpaceRepository;
import com.filespace.domain.user.domain.User;
import com.filespace.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class FileServiceImpl implements FileService {

    private final UserRepository userRepository;
    private final FileRepository fileRepository;
    private final FolderRepository folderRepository;
    private final SpaceRepository spaceRepository;
    private final AmazonS3Client amazonS3Client;
    private final BookmarkFileRepository bookmarkFileRepository;
    private final LogRepository logRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${S3_REGION}")
    private String region;

    @Override
    public String uploadFile(MultipartFile file, Long spaceId, Long folderId, Long userId) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);

        String fileName = UUID.randomUUID().toString() + "." + extension;

        // spaceId로부터 space를 찾고, 해당 space의 bucket_name을 가져옴
        Space space = spaceRepository.findById(spaceId)
                .orElseThrow(() -> new IllegalArgumentException("Not Found : " + spaceId));
        String bucketName = space.getSpaceBucketName();

        // 버킷 이름을 로그로 출력
        System.out.println("Using bucket name: " + bucketName);

        String fileUrl = "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + fileName;
        System.out.println(fileUrl);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        amazonS3Client.putObject(bucketName, fileName, file.getInputStream(), metadata);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Not Found : " + userId));

        File saveFile = null;

        if (folderId != null) {
            Folder folder = folderRepository.findById(folderId)
                    .orElseThrow(() -> new IllegalArgumentException("Not Found : " + folderId));

            saveFile = fileRepository.save(
                    File.builder()
                            .fileName(originalFilename)
                            .fileExtension(extension)
                            .fileCreatedAt(LocalDateTime.now())
                            .fileUpdatedAt(LocalDateTime.now())
                            .filePath(fileUrl)
                            .fileSize(metadata.getContentLength())
                            .fileFolderId(folder)
                            .fileSpaceId(space)
                            .fileUserId(user)
                            .build()
            );
        } else {
            saveFile = fileRepository.save(
                    File.builder()
                            .fileName(originalFilename)
                            .fileExtension(extension)
                            .fileCreatedAt(LocalDateTime.now())
                            .fileUpdatedAt(LocalDateTime.now())
                            .filePath(fileUrl)
                            .fileSize(metadata.getContentLength())
                            .fileFolderId(null)
                            .fileSpaceId(space)
                            .fileUserId(user)
                            .build()
            );
        }


        Log uploadLog = Log.builder()
                .fileName(saveFile.getFileName())
                .logType(LogType.UPLOAD)
                .spaceName(space.getSpaceName())
                .userId(user.getId())
                .build();

        logRepository.save(uploadLog);

        return saveFile.getFilePath();
    }

    private List<FileResponse> mapToFileResponseListWithUserId(List<File> files, User user) {
        List<FileResponse> fileResponses = new ArrayList<>();
        for (File file : files) {
            Boolean isBookmarked = bookmarkFileRepository.existsByBookmarkFileUserIdAndBookmarkFileIdFK(user, file);
            fileResponses.add(FileResponse.of(file, isBookmarked));
        }
        return fileResponses;
    }


    public List<FileResponse> mapToFileResponseList(List<File> files) {
        List<FileResponse> fileResponses = new ArrayList<>();
        for (File file : files) {
            boolean isBookmarked = isFileBookmarked(file.getFileId());
            fileResponses.add(FileResponse.of(file, isBookmarked));
        }
        return fileResponses;
    }


    public boolean isFileBookmarked(Long fileId) {
        File file = fileRepository.findByFileId(fileId)
                .orElseThrow(() -> new IllegalArgumentException("Not Found : " + fileId));
        return bookmarkFileRepository.existsByBookmarkFileIdFK(file);
    }

    @Override
    public List<FileResponse> findAllFilesInFolder(Long folderId, Long userId) {
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new IllegalArgumentException("Not Found : " + folderId));

        List<File> files = fileRepository.findAllByFileFolderIdAndFileDeletedAtIsNull(folder)
                .orElseThrow(() -> new IllegalArgumentException("Not Found : files"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Not Found : " + userId));

        return mapToFileResponseListWithUserId(files, user);
    }

    @Override
    public List<FileResponse> findAllDeletedFilesInFolder(Long folderId) {
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new IllegalArgumentException("Not Found : " + folderId));

        List<File> files = fileRepository.findAllByFileFolderIdAndFileDeletedAtIsNotNull(folder)
                .orElseThrow(() -> new IllegalArgumentException("Not Found : files"));

        return mapToFileResponseList(files);
    }


    @Override
    public FileResponse getFileById(Long fileId) {
        File file = fileRepository.findByFileId(fileId)
                .orElseThrow(() -> new IllegalArgumentException("Not Found : " + fileId));
        boolean isBookmarked = isFileBookmarked(fileId);
        return FileResponse.of(file, isBookmarked);
    }

    @Override
    public FileResponse updateFile(Long fileId, String fileName, Long folderId, String what) throws FileNotFoundException {
        File file = fileRepository.findById(fileId)
                .orElseThrow(() -> new FileNotFoundException("File not found with ID: " + fileId));

        if (Objects.equals(what, "name")) {
            file.rename(fileName);
        }

        if (Objects.equals(what, "path")) {
            if (folderId != null) {
                Folder folder = folderRepository.findById(folderId)
                        .orElseThrow(() -> new IllegalArgumentException("Not Found : " + folderId));
                file.setFileFolderId(folder);
            }

            else {
                file.setFileFolderId(null);
            }
        }

        boolean isBookmarked = isFileBookmarked(fileId);
        File updatedFile = fileRepository.save(file);
        return FileResponse.of(updatedFile, isBookmarked);
    }

    @Override
    public void deleteFile(Long fileId) throws Exception {
        File file = fileRepository.findByFileId(fileId)
                .orElseThrow(() -> new IllegalArgumentException("Not Found : " + fileId));
        if (file.getFilePath() == null) {
            throw new FileNotFoundException("File not found with ID: " + fileId);
        }

        List<BookmarkFile> bookmarkFiles = bookmarkFileRepository.findByBookmarkFileIdFK(file);
        for (BookmarkFile bookmarkFile : bookmarkFiles) {
            bookmarkFileRepository.delete(bookmarkFile);
        }

        bucket = file.getFileSpaceId().getSpaceBucketName();
        String fileName = file.getFilePath().substring(file.getFilePath().lastIndexOf('/') + 1);
        amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, fileName));

        fileRepository.delete(file);

    }

    @Override
    public void trashFile(Long fileId) throws Exception {
        File file = fileRepository.findByFileId(fileId)
                .orElseThrow(() -> new IllegalArgumentException("Not Found : " + fileId));
        if (file.getFilePath() == null) {
            throw new FileNotFoundException("File not found with ID: " + fileId);
        }

        List<BookmarkFile> bookmarkFiles = bookmarkFileRepository.findByBookmarkFileIdFK(file);
        for (BookmarkFile bookmarkFile : bookmarkFiles) {
            bookmarkFileRepository.delete(bookmarkFile);
        }

        if (file.getFileDeletedAt() == null) {
            file.setFileDeletedAt(LocalDateTime.now());
            fileRepository.save(file);
        }
    }

    @Override
    public List<FileResponse> findAllRootFiles(Long spaceId, Long userId) {
        Space space = spaceRepository.findById(spaceId)
                .orElseThrow(() -> new IllegalArgumentException("Not Found : " + spaceId));
        System.out.println("spaceId : " + spaceId);
        List<File> rootFiles = fileRepository.findAllByFileSpaceIdAndFileFolderIdIsNullAndFileDeletedAtIsNull(space)
                .orElseThrow(() -> new IllegalArgumentException("Not Found Files : " + spaceId));
        System.out.println("rootFiles : " + rootFiles);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Not Found : " + userId));

        return mapToFileResponseListWithUserId(rootFiles, user);
    }

    @Override
    public List<FileResponse> findAllFilesBySpaceId (Long spaceId) {
        Space space = spaceRepository.findById(spaceId)
                .orElseThrow(() -> new IllegalArgumentException("Not Found : " + spaceId));
        List<File> files = fileRepository.findAllByFileSpaceIdAndFileDeletedAtIsNull(space)
                .orElseThrow(() -> new IllegalArgumentException("Not Found Files : " + spaceId));

        return mapToFileResponseList(files);
    }

    @Override
    public void createDownloadLog (Long fileId, LogRequest.createLog data) {
        Long userId = data.getUserId();
        Long spaceId = data.getSpaceId();

        // todo : authorization check
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Not Found : " + userId));
        File file = fileRepository.findByFileId(fileId)
                .orElseThrow(() -> new IllegalArgumentException("Not Found : " + fileId));
        Space space = spaceRepository.findById(data.getSpaceId())
                .orElseThrow(() -> new IllegalArgumentException("Not Found : " + spaceId));

        Log downloadLog = Log.builder()
                .fileName(file.getFileName())
                .logType(LogType.DOWNLOAD)
                .spaceName(space.getSpaceName())
                .userId(user.getId())
                .build();

        logRepository.save(downloadLog);
    }
}

