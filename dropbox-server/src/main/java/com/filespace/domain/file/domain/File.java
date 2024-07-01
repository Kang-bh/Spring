package com.filespace.domain.file.domain;

import com.filespace.domain.folder.domain.Folder;
import com.filespace.domain.space.domain.Space;
import com.filespace.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Table(name="files")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@Entity
@EntityListeners(AuditingEntityListener.class)
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="file_id", updatable = false)
    private Long fileId;

    @Column(name="file_name", updatable = true)
    private String fileName;

    @Column(name="extension", updatable = true)
    private String fileExtension;

    @CreatedDate
    @Column(name="created_at")
    private LocalDateTime fileCreatedAt;

    @LastModifiedDate
    @Column(name="updated_at")
    private LocalDateTime fileUpdatedAt;

    @Column(name="deleted_at")
    private LocalDateTime fileDeletedAt;

    @Column(name="file_path", updatable = true)
    private String filePath;

    @Column(name="file_size", updatable = true)
    private Long fileSize;

    @ManyToOne
    @JoinColumn(name="space_id") // , nullable=false
    private Space fileSpaceId;

    @ManyToOne
    @JoinColumn(name="folder_id") // , nullable=false
    private Folder fileFolderId;

    @ManyToOne
    @JoinColumn(name="user_id") // , nullable=false
    private User fileUserId;

    @Builder
    public File(Long fileId, String fileName, String fileExtension, LocalDateTime fileCreatedAt, LocalDateTime fileUpdatedAt, LocalDateTime fileDeletedAt, String filePath, Long fileSize, Space fileSpaceId, Folder fileFolderId, User fileUserId) {
        this.fileId = fileId;
        this.fileName = fileName;
        this.fileExtension = fileExtension;
        this.fileCreatedAt = fileCreatedAt;
        this.fileUpdatedAt = fileUpdatedAt;
        this.fileDeletedAt = fileDeletedAt;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.fileSpaceId = fileSpaceId;
        this.fileFolderId = fileFolderId;
        this.fileUserId = fileUserId;
    }

    public void rename(String name) {
        this.fileName = name;
    }

    public void setFileDeletedAt(LocalDateTime deletedAt) { this.fileDeletedAt = deletedAt; }

    public void setFileFolderId(Folder fileFolderId) {
        this.fileFolderId = fileFolderId;
    }
}
