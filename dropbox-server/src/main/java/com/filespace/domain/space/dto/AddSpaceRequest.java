package com.filespace.domain.space.dto;

import com.filespace.domain.space.enumuration.SpaceType;
import com.filespace.domain.user.domain.User;
import lombok.*;
import lombok.AllArgsConstructor;
import com.filespace.domain.space.domain.Space;

import java.util.List;
import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AddSpaceRequest {
    private Long totalStorage;
    private SpaceType type;
    private Boolean isPublic;
    private String name;
    private List<String> label;
    private Long ownerId;
    private Long maxPeople;
    private String password;

    public Space toEntity(User creator, String bucketName) { // User creatorId
        String stringLabel = getStringLabel(label); // null 체크 후 문자열 결합
        // Dto to Entity
        return Space.builder()
                .isPublic(isPublic)
                .label(stringLabel)
                .totalStorage(totalStorage)
                .type(type)
                .ownerId(creator)
                .password(password)
                .maxPeople(maxPeople)
                .name(name)
                .spaceBucketName(bucketName)
                .build();
    }

    private static String getStringLabel(List<String> label) {
        if (label == null) {
            System.out.println(2);
            return null;
        }
        System.out.println(2.5);
        return String.join(", ", label);
    }
}
