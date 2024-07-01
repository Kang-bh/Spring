package com.filespace.domain.space.repository;

import com.filespace.domain.file.domain.File;
import com.filespace.domain.space.domain.Log;
import com.filespace.domain.space.domain.Space;
import com.filespace.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LogRepository  extends JpaRepository<Log, Long> {
    @Query
    List<Log> findByLogFileName(String fileName);
    List<Log> findAllByLogUserId (Long userId);
}
