package com.filespace.domain.file.domain;

import com.filespace.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Table(name="bookmark_files")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@Entity
@EntityListeners(AuditingEntityListener.class)
public class BookmarkFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="bookmark_file_id", updatable = false)
    private Long bookmarkFileId;

    @ManyToOne
    @JoinColumn(name="user_id", updatable = false)
    private User bookmarkFileUserId;

    @ManyToOne
    @JoinColumn(name="file_id", updatable = false)
    private File bookmarkFileIdFK;

    @Builder
    public BookmarkFile(User bookmarkFileUserId, File bookmarkFileIdFK) {
        this.bookmarkFileUserId = bookmarkFileUserId;
        this.bookmarkFileIdFK = bookmarkFileIdFK;
    }
}
