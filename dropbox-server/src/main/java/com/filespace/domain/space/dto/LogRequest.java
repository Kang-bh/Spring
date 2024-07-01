package com.filespace.domain.space.dto;

import com.filespace.domain.file.domain.File;
import com.filespace.domain.space.domain.Log;
import com.filespace.domain.space.domain.Space;
import com.filespace.domain.space.enumuration.LogType;
import com.filespace.domain.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter

public class LogRequest {

    @Getter
    @Setter
    public static class createLog {
        private Long spaceId;
        private Long userId;
    }
}
