package com.filespace.domain.space.dto;

import com.filespace.domain.space.domain.Log;
import com.filespace.domain.space.enumuration.LogType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Schema(description = "Log response DTO")
public class LogResponse {

    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    static public class LogInfo {

        private final Long logId;
        private final LogType type;
        private final String spaceName;
        private final String fileName;
        private final LocalDateTime createdAt;

        public static LogResponse.LogInfo of(Log log) {
            return LogInfo.builder()
                    .logId(log.getLogId())
                    .type(log.getLogType())
                    .spaceName(log.getLogSpaceName())
                    .fileName(log.getLogFileName())
                    .createdAt(log.getLogCreatedAt())
                    .build();
        }
    }
}
