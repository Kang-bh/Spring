package com.filespace.domain.folder.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Table(name="folders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Folder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="folder_id", updatable = false)
    private Long folderId;

    @Column(name="folder_name")
    private String folderName;

    @Column(name="folder_size")
    private Long folderSize;

    @Column(name = "trash")
    private boolean trash;

    @CreatedDate
    @Column(name="created_at")
    private LocalDateTime folderCreatedAt;

    @LastModifiedDate
    @Column(name="updated_at")
    private LocalDateTime folderUpdatedAt;

    @Column(name="deleted_at")
    private LocalDateTime folderDeletedAt;

    // 추후 self-referencing
    @Column(name="parent_folder_id")
    private Long folderParentId;

    // 추후 연관관계 매핑
//     @ManyToOne
//     @JoinColumn(name="user_id", nullable=false)
    @Column(name="user_id")
     private Long userId;

    // 추후 연관관계 매핑
//     @ManyToOne
//     @JoinColumn(name="space_id", nullable = false)
    @Column(name="space_id")
     private Long spaceId;

    @Builder
    public Folder(String name, Long folderUserId, Long folderSpaceId, Long folderParentId) {
        this.folderName = name;
        this.userId = folderUserId;
        this.spaceId = folderSpaceId;
        this.folderParentId = folderParentId;
    }

    public void rename(String name) {
        this.folderName = name;
    }

    public void modifyPath(Long newParentId) {
            this.folderParentId = newParentId;
    }

    public void setFolderDeletedAt(LocalDateTime deletedAt) {
        this.folderDeletedAt = deletedAt;
    }

    public void setTrash(boolean isTrash) {
        this.trash = isTrash;
    }
}
