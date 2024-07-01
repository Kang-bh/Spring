package com.filespace.domain.space.dto;

import com.filespace.domain.space.domain.Participant;
import com.filespace.domain.user.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

// todo :delete
@Schema(description = "participant response dto")
public class ParticipantResponse {

    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    // todo : user 정보의 추가 가능성 존재
    static public class ParticipantInfo {
        private final User participant;
        private final Boolean isFirstEntry;

        public static ParticipantResponse.ParticipantInfo of(User user, Boolean isFirstEntry) {
            return ParticipantInfo.builder()
                    .participant(user)
                    .isFirstEntry(isFirstEntry)
                    .build();
        }
    }
}
