package com.filespace.domain.space.repository;

import com.filespace.domain.space.domain.Participant;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long>{
    List<Participant> findAllByParticipantSpaceId_SpaceId(Long spaceId);
    List<Participant> findAllByParticipantUserId_Id(Long userId);

    Participant findByParticipantSpaceId_SpaceIdAndParticipantUserId_Id(Long spaceId, Long userId);
    void deleteByParticipantSpaceId_SpaceIdAndParticipantUserId_Id(Long spaceId, Long userId);
    void deleteByParticipantSpaceId_SpaceId(Long spaceId);

    Long countByParticipantSpaceId_SpaceId(Long spaceId);
}
