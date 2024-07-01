package com.filespace.domain.file.repository;

import com.filespace.domain.file.domain.File;
import com.filespace.domain.folder.domain.Folder;
import com.filespace.domain.space.domain.Space;
import com.filespace.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    @Query
    Optional<List<File>> findAllByFileFolderId(Folder fileFolderId);

    @Query
    Optional<File> findByFileId(Long fileId);

    @Query
    List<File> findAllByFileDeletedAtIsNotNull();

    @Query
    List<File> findAllByFileUserIdAndFileDeletedAtIsNotNull(Optional<User> user);

    @Query
    Optional<List<File>> findAllByFileFolderIdAndFileDeletedAtIsNull(Folder fileFolderId);

    @Query
    Optional<List<File>> findAllByFileFolderIdAndFileDeletedAtIsNotNull(Folder fileFolderId);

    @Query
    Optional<List<File>> findAllByFileSpaceIdAndFileFolderIdIsNullAndFileDeletedAtIsNull(Space spaceId);

    @Query
    Optional<List<File>> findAllByFileSpaceIdAndFileDeletedAtIsNull(Space spaceId);

    void deleteAllByFileSpaceId(Space spaceId);
}