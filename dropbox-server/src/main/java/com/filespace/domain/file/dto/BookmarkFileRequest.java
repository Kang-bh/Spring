package com.filespace.domain.file.dto;

import com.filespace.domain.file.domain.BookmarkFile;
import com.filespace.domain.file.domain.File;
import com.filespace.domain.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class BookmarkFileRequest {

    private User bookmarkFileUserId;
    private File bookmarkFileIdFK;

    // Dto to Entity
    public BookmarkFile toEntity() {
        return BookmarkFile.builder()
                .bookmarkFileUserId(bookmarkFileUserId)
                .bookmarkFileIdFK(bookmarkFileIdFK)
                .build();
    }
}
