package com.filespace.domain.dashboard.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.filespace.domain.dashboard.dto.DashBoardResponse;
import com.filespace.domain.dashboard.enumuration.ExtensionType;
import com.filespace.domain.file.domain.File;
import com.filespace.domain.file.dto.FileResponse;
import com.filespace.domain.file.repository.FileRepository;
import com.filespace.domain.space.domain.Log;
import com.filespace.domain.space.domain.Space;
import com.filespace.domain.space.dto.LogResponse;
import com.filespace.domain.space.dto.SpaceResponse;
import com.filespace.domain.space.repository.LogRepository;
import com.filespace.domain.space.repository.SpaceRepository;
import com.filespace.domain.user.domain.User;
import com.filespace.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class DashBoardServiceImpl implements DashBoardService{

    // todo: s3로 해보기
//    private final AmazonS3Client amazonS3Client;
//
//    public String getSpaceStorageRatio(SpaceResponse.SpaceInfo space) {
//        amazonS3Client.g
//
//    }
    private final UserRepository userRepository;
    private final LogRepository logRepository;
    private final FileRepository fileRepository;
    private final SpaceRepository spaceRepository;

    // logic
    public DashBoardResponse.spaceStorageInfo getSpaceStorageUsage (List<FileResponse> files) {

        Map<ExtensionType, Long> sizeByExtensions = setInitialExtension();

        for (FileResponse file : files) {
            String extension = file.getFileExtension();
            Long size = file.getFileSize();
            ExtensionType type = ExtensionType.findByExtension(extension);
            System.out.println("type : " + type);
            sizeByExtensions.merge(type, size, Long::sum);
        }


        sizeByExtensions.replaceAll((type, size) -> size == null ? 0L : size);

        return DashBoardResponse.spaceStorageInfo.of(sizeByExtensions);
    }

    public List<LogResponse.LogInfo> getLogs (Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Not found with ID: " + userId));

        List<Log> logs = logRepository.findAllByLogUserId(user.getId());

        return createLogResponseList(logs);
    }

    private List<LogResponse.LogInfo> createLogResponseList(List<Log> logs) {
        List<LogResponse.LogInfo> logResponses = new ArrayList<>();
        for (Log log : logs) {
            logResponses.add(LogResponse.LogInfo.of(log));
        }

        return logResponses;
    }

    private static Map<ExtensionType, Long> setInitialExtension () {
        Map<ExtensionType, Long> sizeByExtensions = new HashMap<>();

        sizeByExtensions.put(ExtensionType.IMAGE, 0L);
        sizeByExtensions.put(ExtensionType.AUDIO, 0L);
        sizeByExtensions.put(ExtensionType.DOC, 0L);
        sizeByExtensions.put(ExtensionType.ZIP, 0L);
        sizeByExtensions.put(ExtensionType.OTHER, 0L);

        return sizeByExtensions;
    }
}
