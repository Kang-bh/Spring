package com.filespace.domain.file.repository;

import com.filespace.domain.file.domain.BookmarkFile;
import com.filespace.domain.file.domain.File;
import com.filespace.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookmarkFileRepository extends JpaRepository<BookmarkFile, Long> {
    @Query
    BookmarkFile findByBookmarkFileUserIdAndBookmarkFileIdFK(User bookmarkFileUserId, File bookmarkFileIdFK);

    @Query
    List<BookmarkFile> findByBookmarkFileUserId(User bookmarkFileUserId);

    @Query
    Boolean existsByBookmarkFileIdFK(File bookmarkFileIdFK);

    @Query
    List<BookmarkFile> findByBookmarkFileIdFK(File bookmarkFileIdFK);

    @Query
    Boolean existsByBookmarkFileUserIdAndBookmarkFileIdFK(User bookmarkFileUserId, File bookmarkFileIdFK);
}
