package com.filespace.domain.file.service;

import com.filespace.domain.file.domain.BookmarkFile;
import com.filespace.domain.file.domain.File;
import com.filespace.domain.file.dto.BookmarkFileResponse;
import com.filespace.domain.file.repository.BookmarkFileRepository;
import com.filespace.domain.file.repository.FileRepository;
import com.filespace.domain.user.domain.User;
import com.filespace.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BookmarkFileService {

    private final UserRepository userRepository;
    private final BookmarkFileRepository bookmarkFileRepository;
    private final FileRepository fileRepository;

    // 파일 즐겨찾기 (추가, 삭제)
    public void bookmarkFile(Long userId, Long fileId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Not Found : " + userId));
        File file = fileRepository.findByFileId(fileId)
                .orElseThrow(() -> new IllegalArgumentException("Not Found : " + fileId));

        BookmarkFile existingBookmark = bookmarkFileRepository.findByBookmarkFileUserIdAndBookmarkFileIdFK(user, file);

        if (existingBookmark != null) {
            bookmarkFileRepository.delete(existingBookmark);
        } else {
            BookmarkFile saveBookmarkFile = bookmarkFileRepository.save(
                    BookmarkFile.builder()
                            .bookmarkFileIdFK(file)
                            .bookmarkFileUserId(user)
                            .build()
            );
        }
    }


    public List<BookmarkFileResponse> getBookmarkFilesByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Not Found : " + userId));
        List<BookmarkFile> bookmarkFiles = bookmarkFileRepository.findByBookmarkFileUserId(user);

        return mapToBookmarkFileResponseList(bookmarkFiles);
    }

    private List<BookmarkFileResponse> mapToBookmarkFileResponseList(List<BookmarkFile> bookmarkFiles) {
        List<BookmarkFileResponse> bookmarkFileResponses = new ArrayList<>();
        for(BookmarkFile bookmarkFile : bookmarkFiles) {
            bookmarkFileResponses.add(BookmarkFileResponse.of(bookmarkFile));
        }
        return bookmarkFileResponses;
    }

}
