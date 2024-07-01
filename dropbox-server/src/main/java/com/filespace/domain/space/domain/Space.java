package com.filespace.domain.space.domain;

import com.filespace.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.UUID;

import com.filespace.domain.space.enumuration.SpaceType;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Table(name="spaces")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Space {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "space_id")
    private Long spaceId;

    @Column(name="name", updatable = true)
    private String spaceName;

    @Column(name="total_storage", updatable = true)
    private Long spaceTotalStorage;

    @Column(name="type")
    @Enumerated(EnumType.STRING)
    private SpaceType spaceType;

    @Column(name="is_public")
    private Boolean spaceIsPublic;

    @Column(name="max_people", updatable = true)
    private Long spaceMaxPeople;

    @Column(name="label", updatable = true)
    private String spaceLabel; // list to stringify

    @Column(name="bucket_name", updatable = false)
    private String spaceBucketName;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime spaceCreatedAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime spaceUpdatedAt;

    @ManyToOne
    @JoinColumn(name="user_id", updatable = false)
    private User ownerId;

    @Column(name = "password")
    private String spacePassword;

    @Builder
    public Space(Long totalStorage, SpaceType type, Boolean isPublic, String name, String label, User ownerId, Long maxPeople, String password, String spaceBucketName) {
        this.spaceTotalStorage = totalStorage;
        this.spaceType = type;
        this.spaceIsPublic = isPublic;
        this.spaceName = name;
        this.spaceLabel = label;
        this.ownerId = ownerId;
        this.spaceMaxPeople = maxPeople;
        this.spacePassword = password;
        this.spaceBucketName = spaceBucketName;
    }

    public void update(String name, String labels, Long maxPeople) {
        this.spaceName = name;
        this.spaceLabel = labels;
        this.spaceMaxPeople = maxPeople;
    }
}