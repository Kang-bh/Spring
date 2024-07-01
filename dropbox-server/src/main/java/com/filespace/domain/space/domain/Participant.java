package com.filespace.domain.space.domain;

import com.filespace.domain.user.domain.User;
import io.micrometer.core.annotation.Counted;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Optional;

@Table(name="participants")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="participant_id", updatable = false)
    private Long participantId;

    @ManyToOne
    @JoinColumn(name="space_id", nullable = false)
    private Space participantSpaceId;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User participantUserId;

    @Column(name="is_first_entry", nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean isFirstEntry;

    @Builder
    public Participant(Space space, User user) {
        this.participantUserId = user;
        this.participantSpaceId = space;
        this.isFirstEntry = false;
    }

    // todo : 고려
//    public Long getUserId() {
//        return participantUserId.getId(); // 이 경우 User 엔티티에서 사용자 ID를 가져오는 메서드를 호출해야 합니다. 사용자 엔티티에 getId() 메서드가 있다고 가정합니다.
//    }

}
