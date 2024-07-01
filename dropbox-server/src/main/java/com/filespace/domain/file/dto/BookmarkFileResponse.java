package com.filespace.domain.file.dto;

import com.filespace.domain.file.domain.BookmarkFile;
import com.filespace.domain.file.domain.File;
import com.filespace.domain.user.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookmarkFileResponse {
    private Long bookmarkId;
    private User bookmarkFileUserId;
    private File bookmarkFileIdFK;

    public static BookmarkFileResponse of(BookmarkFile bookmarkFile) {
        return BookmarkFileResponse.builder()
                .bookmarkId(bookmarkFile.getBookmarkFileId())
                .bookmarkFileUserId(bookmarkFile.getBookmarkFileUserId())
                .bookmarkFileIdFK(bookmarkFile.getBookmarkFileIdFK()).build();
    }
}
