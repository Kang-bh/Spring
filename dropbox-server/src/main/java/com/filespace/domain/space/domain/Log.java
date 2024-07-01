package com.filespace.domain.space.domain;

import com.filespace.domain.file.domain.File;
import com.filespace.domain.space.enumuration.LogType;
import com.filespace.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Table(name="logs")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="log_id", updatable = false)
    private Long logId;

    @Column(name="type")
    private LogType logType;

    @CreatedDate
    @Column(name="created_at")
    private LocalDateTime logCreatedAt;

    @Column(name="user_id", nullable=false)
    private Long logUserId;

    @Column(name="space_name", nullable = false)
    private String logSpaceName;

    @Column(name="file_name", nullable = false)
    private String logFileName;

    @Builder
    public Log(LogType logType, String spaceName, Long userId, String fileName) {
        this.logType = logType;
        this.logSpaceName = spaceName;
        this.logUserId = userId;
        this.logFileName = fileName;
    }
}
