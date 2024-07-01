package com.filespace.domain.dashboard.dto;

import com.filespace.domain.dashboard.enumuration.ExtensionType;
import com.filespace.domain.dashboard.service.DashBoardService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

public class DashBoardResponse {

    @Builder
    @Getter
    @AllArgsConstructor
    public static class spaceStorageInfo {
        private final Long ImageSize;
        private final Long AudioSize;
        private final Long DocSize;
        private final Long ZipSize;
        private final Long OtherSize;

        //todo : of 쓰는 이유
        public static DashBoardResponse.spaceStorageInfo of(Map<ExtensionType, Long> sizeObj) {
            return spaceStorageInfo.builder()
                    .ImageSize(sizeObj.get(ExtensionType.IMAGE))
                    .AudioSize(sizeObj.get(ExtensionType.AUDIO))
                    .DocSize(sizeObj.get(ExtensionType.DOC))
                    .ZipSize(sizeObj.get(ExtensionType.ZIP))
                    .OtherSize(sizeObj.get(ExtensionType.OTHER))
                    .build();
        }

    }
}
