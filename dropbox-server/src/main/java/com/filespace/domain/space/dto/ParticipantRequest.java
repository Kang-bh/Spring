package com.filespace.domain.space.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public class ParticipantRequest {

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    static public class EnterSpace {
        private String password;
    }
}
