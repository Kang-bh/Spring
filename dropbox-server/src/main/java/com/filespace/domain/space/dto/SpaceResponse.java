package com.filespace.domain.space.dto;

import com.filespace.domain.space.domain.Participant;
import com.filespace.domain.space.domain.Space;
import com.filespace.domain.space.enumuration.SpaceType;
import com.filespace.domain.user.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Schema(description = "space response dto")
public class SpaceResponse {

    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    static public class SpaceInfo {

        private final Long spaceId;
        private final Long totalStorage;
        private final SpaceType type;
        private final Boolean isPublic;
        private final String name;
        private final List<String> label;
        private final Long maxPeople;
        private final User owner; // todo : user dto로 변경
//        private final List<Participant> participants; // todo :  participants dtd
        private final Long numOfPeople;

        public static SpaceResponse.SpaceInfo of(Space space, Long numOfPeople) {
            return SpaceInfo.builder()
                    .spaceId(space.getSpaceId())
                    .totalStorage(space.getSpaceTotalStorage())
                    .type(space.getSpaceType())
                    .isPublic(space.getSpaceIsPublic())
                    .name(space.getSpaceName())
                    .label(space.getSpaceLabel() != null ? Arrays.asList(space.getSpaceLabel().split(",")) : Collections.emptyList())
                    .maxPeople(space.getSpaceMaxPeople())
                    .owner(space.getOwnerId())
                    .numOfPeople(numOfPeople)
                    .build();
        }
    }

}
