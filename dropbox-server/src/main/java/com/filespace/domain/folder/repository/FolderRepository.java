package com.filespace.domain.folder.repository;

import com.filespace.domain.folder.domain.Folder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FolderRepository extends JpaRepository<Folder, Long> {
    List<Folder> findByFolderParentIdAndFolderDeletedAtIsNull(Long folderId);
    List<Folder> findByFolderParentIdAndFolderDeletedAtIsNotNull(Long folderId);
    List<Folder> findAllByTrashIsTrue();
    List<Folder> findAllByUserIdAndTrashIsTrue(Long userId);
    List<Folder> findAllByFolderParentIdIsNullAndFolderDeletedAtIsNullAndSpaceId(Long spaceId);

    void deleteAllBySpaceId(Long spaceId);
}
