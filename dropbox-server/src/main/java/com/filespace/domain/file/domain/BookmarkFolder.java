package com.filespace.domain.file.domain;

import com.filespace.domain.folder.domain.Folder;
import com.filespace.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Table(name="bookmark_folders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@Entity
@EntityListeners(AuditingEntityListener.class)
public class BookmarkFolder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="bookmark_folder_id", updatable = false)
    private Long bookmarkFolderId;

    @ManyToOne
    @JoinColumn(name="user_id", updatable = false)
    private User bookmarkFileUserId;

    @ManyToOne
    @JoinColumn(name="folder_id", updatable = false)
    private Folder bookmarkFileIdFK;
}
