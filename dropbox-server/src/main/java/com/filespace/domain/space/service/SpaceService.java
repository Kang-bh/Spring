package com.filespace.domain.space.service;

import com.filespace.domain.file.repository.FileRepository;
import com.filespace.domain.folder.repository.FolderRepository;
import com.filespace.domain.space.domain.Participant;
import com.filespace.domain.space.domain.Space;
import com.filespace.domain.space.dto.AddSpaceRequest;
import com.filespace.domain.space.dto.SpaceResponse;
import com.filespace.domain.space.dto.UpdateSpaceRequest;
import com.filespace.domain.space.enumuration.SpaceType;
import com.filespace.domain.space.repository.ParticipantRepository;
import com.filespace.domain.space.repository.SpaceRepository;
import com.filespace.domain.space.util.AESCrypt;
import com.filespace.domain.space.util.S3BucketCreator;
import com.filespace.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.expression.AccessException;
import org.springframework.expression.spel.ast.NullLiteral;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import com.filespace.domain.user.domain.User;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class SpaceService {

    private final SpaceRepository spaceRepository;
    // todo : delete
    private final UserRepository userRepository;
    private final FileRepository fileRepository;
    private final FolderRepository folderRepository;
    private final ParticipantRepository participantRepository;
    private final S3BucketCreator s3BucketCreator;
    private final AESCrypt crypt;

    @Transactional(isolation = Isolation.DEFAULT)
    public Space save(AddSpaceRequest spaceRequest, Long userId) {

        if (!spaceRequest.getIsPublic() && spaceRequest.getType() == SpaceType.SHARE) {
            if (spaceRequest.getPassword() == null) {
                throw new IllegalArgumentException("Required password for creating private share space");
            }

            String encryptPassword = crypt.encrypt(spaceRequest.getPassword());

            spaceRequest.setPassword(encryptPassword);
        } else if (spaceRequest.getIsPublic() && spaceRequest.getType() == SpaceType.SHARE && spaceRequest.getPassword() != null) {
            throw new IllegalArgumentException("Not required password for public share space");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Not Found : " + userId));
        // todo : optional vs orElseTrow
        String bucketName = s3BucketCreator.createBucket();

        Space spaceEntity = spaceRequest.toEntity(user, bucketName);

        Space result = spaceRepository.save(spaceEntity);

        Participant participant = Participant.builder()
                .user(user)
                .space(spaceEntity)
                .build();

        participantRepository.save(participant);

        return result;

    }
    @Transactional(readOnly = true)
    public List<SpaceResponse.SpaceInfo> findAllSpace(Long userId, Boolean isParticipated) {
        List<SpaceResponse.SpaceInfo> spaceInfoList;
        // isParticipated false이면 private안뜨게

        List<Participant> participantList = participantRepository.findAllByParticipantUserId_Id(userId);

        spaceInfoList = participantList.stream()
                .map(participant -> {
                    Space space = spaceRepository.findById(participant.getParticipantSpaceId().getSpaceId())
                            .orElseThrow(() -> new IllegalArgumentException("Not Found or Space is Private: " + participant.getParticipantSpaceId().getSpaceId()));

                    Long participantCount = participantRepository.countByParticipantSpaceId_SpaceId(space.getSpaceId());

                    return SpaceResponse.SpaceInfo.of(space, participantCount);
                })
                .collect(Collectors.toList());

        if (!isParticipated) {

            // todo : refactor
            List<Long> participantSpaceList = spaceInfoList
                    .stream()
                    .map(space -> {
                        return space.getSpaceId();
                    })
                    .collect(Collectors.toList());

            List<Space> nonParticipatedSpaces = spaceRepository.findAll()
                    .stream()
                    .filter(space -> space.getSpaceType() == SpaceType.SHARE && !participantSpaceList.contains(space.getSpaceId()))
                    .collect(Collectors.toList());

            spaceInfoList = nonParticipatedSpaces
                    .stream()
                    .map(space -> {
                        Long participantCount = participantRepository.countByParticipantSpaceId_SpaceId(space.getSpaceId());

                        return SpaceResponse.SpaceInfo.of(space, participantCount);
                    })
                    .collect(Collectors.toList());
        }

        return spaceInfoList;

//        List<Space> spaceList = spaceRepository.findAll();


        // todo : code refactor
//        List<SpaceResponse.SpaceInfo> spaceInfoList = spaceList.stream()
//                .map(space -> {
//                    System.out.println(space);
//
//                    return SpaceResponse.SpaceInfo.of(space);
//                })
//                .collect(Collectors.toList());

    }

    @Transactional(readOnly = true)
    public SpaceResponse.SpaceInfo findSpaceById(long spaceId) {
        Space space = spaceRepository.findById(spaceId)
                .orElseThrow(() -> new IllegalArgumentException("Not Found : " + spaceId));

//        List<Participant> participants = participantRepository.findAllBySpace_SpaceId(space.getSpaceId());
        Long participantCount = participantRepository.countByParticipantSpaceId_SpaceId(space.getSpaceId());

        return SpaceResponse.SpaceInfo.of(space, participantCount);
    }

    @Transactional(isolation = Isolation.DEFAULT)
    public void deleteSpaceById(long spaceId, long userId) throws AccessException {
        // todo : error log
        Space space = spaceRepository.findById(spaceId)
                .orElseThrow(() -> new IllegalArgumentException("Not Found : " + spaceId));

        if (space.getOwnerId().getId() != userId) {
            throw new AccessException("No permission of delete space");
        }

        // todo : user 구현 확인 후 처리
        //        authorizeSpaceOwner(space);
        // todo : db rollback option or logic 유지
        // todo : bucket 삭제

        participantRepository.deleteByParticipantSpaceId_SpaceId(spaceId);
        fileRepository.deleteAllByFileSpaceId(space);
        folderRepository.deleteAllBySpaceId(spaceId);
        spaceRepository.deleteById(spaceId);
    }

    @Transactional(isolation = Isolation.DEFAULT)
    public SpaceResponse.SpaceInfo updateSpace(long spaceId, UpdateSpaceRequest spaceRequest, long userId) throws AccessException {
        Space space = spaceRepository.findById(spaceId)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + spaceId));

        if (userId != space.getOwnerId().getId()) {
            throw new AccessException("No permission of update space");
        }

        // todo : refactor
        String name = spaceRequest.getName();
        String labels = spaceRequest.getLabel();
        Long maxPeople = spaceRequest.getMaxPeople();

        if (name == null) {
            name = space.getSpaceName();
        }

        if (labels == null) {
            labels = space.getSpaceLabel();
        }

        if (maxPeople == null) {
            maxPeople = space.getSpaceMaxPeople();
        }

        System.out.println(name);
        System.out.println(labels);
        System.out.println(maxPeople);
        space.update(name, labels, maxPeople);

        Space result = spaceRepository.save(space);

        Long participantCount = participantRepository.countByParticipantSpaceId_SpaceId(space.getSpaceId());
        return SpaceResponse.SpaceInfo.of(result, participantCount);
    }

        // todo : user 구현 확인 후 처리
        //    private static void authorizeSpaceOwner(Space space) {
        //        Long ownerId = SecurityContextHolder.getContext().getAuthentication().getID();
        //    }

    public SpaceType checkSpaceType(Long spaceId) {
        Space space = spaceRepository.findById(spaceId)
                .orElseThrow(() -> new IllegalArgumentException("Not Found : " + spaceId));

        return space.getSpaceType();

    }

    @Transactional(readOnly = true)
    public SpaceResponse.SpaceInfo getOwnSpace(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Not Found : " + userId));

        Space space = spaceRepository.findOwnSpace(user, SpaceType.PRIVATE);
        Long participantCount = participantRepository.countByParticipantSpaceId_SpaceId(space.getSpaceId());

        return SpaceResponse.SpaceInfo.of(space, participantCount);
    }
}

