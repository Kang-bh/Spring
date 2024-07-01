package com.filespace.domain.space.service;

import com.filespace.domain.space.domain.Participant;
import com.filespace.domain.space.domain.Space;
//import com.filespace.domain.space.dto.AddParticipantRequest;
import com.filespace.domain.space.dto.ParticipantRequest;
import com.filespace.domain.space.dto.ParticipantResponse;
import com.filespace.domain.space.repository.ParticipantRepository;
import com.filespace.domain.space.repository.SpaceRepository;
import com.filespace.domain.space.util.AESCrypt;
import com.filespace.domain.user.domain.User;
import com.filespace.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ParticipantService {

    private final ParticipantRepository participantRepository;
    private final SpaceRepository spaceRepository;
    private final UserRepository userRepository;
    private final AESCrypt crypt;

    // todo : consider response type
    @Transactional(isolation = Isolation.DEFAULT)
    public void save(Long spaceId, Long userId, Optional<ParticipantRequest.EnterSpace> body) {
        Space space = spaceRepository.findById(spaceId)
                .orElseThrow(() -> new IllegalArgumentException("Not Found : " + spaceId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Not Found : " + userId));

        Participant participant = participantRepository.findByParticipantSpaceId_SpaceIdAndParticipantUserId_Id(spaceId, userId);

        if (participant != null) {
            // todo : change exception
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Already participate in space"
            );
        }

        if (space.getSpaceIsPublic() == false) { // 비밀 방
            String password = body.map(ParticipantRequest.EnterSpace::getPassword).orElse(null);

            if(password == null) {
                throw new IllegalArgumentException("Check whether space is private");
            }

            if(!crypt.encrypt(password).equals(space.getSpacePassword())) {
                System.out.println(crypt.encrypt(password));
                System.out.println(crypt.encrypt(password).getClass().getName());
                System.out.println(space.getSpacePassword());
                System.out.println(space.getSpacePassword().getClass().getName());
                System.out.println(crypt.encrypt(password) == space.getSpacePassword());
                throw new IllegalArgumentException("Check Password");
            }
        }

        Participant participantEntity = Participant.builder()
                .user(user)
                .space(space)
                .build();

        participantRepository.save(participantEntity);
    }


    @Transactional(isolation = Isolation.DEFAULT)
    public void exit(Long spaceId, Long userId) {
        Space space = spaceRepository.findById(spaceId)
                .orElseThrow(() -> new IllegalArgumentException("Not Found : " + spaceId));

        if (space.getOwnerId().getId() == userId) {
            // todo: 오너에게 해당 interface존재하는지 확인 일단 throw로 처리
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "owner"
            );
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Not Found : " + userId));


        Participant participant = participantRepository.findByParticipantSpaceId_SpaceIdAndParticipantUserId_Id(spaceId, userId);

        if (participant == null) {
            // todo : change exception
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "No Participant in space"
            );
        }

        participantRepository.deleteByParticipantSpaceId_SpaceIdAndParticipantUserId_Id(spaceId, userId);
    }

    @Transactional(readOnly = true)
    public List<ParticipantResponse.ParticipantInfo> findAllParticipants(Long spaceId) {
        Space space = spaceRepository.findById(spaceId)
                .orElseThrow(() -> new IllegalArgumentException("Not Found : " + spaceId));

        List<Participant> participantList = participantRepository.findAllById(Collections.singleton(spaceId)); // todo : study mean

        List<ParticipantResponse.ParticipantInfo> participantInfoList = participantList
                .stream()
                .map(participant -> {
                    return ParticipantResponse.ParticipantInfo.of(participant.getParticipantUserId(), participant.getIsFirstEntry());
                })
                .collect(Collectors.toList());

        return participantInfoList;
    }

}
