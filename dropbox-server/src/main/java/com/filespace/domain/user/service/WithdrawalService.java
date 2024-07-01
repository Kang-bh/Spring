package com.filespace.domain.user.service;

import com.filespace.domain.folder.domain.Folder;
import com.filespace.domain.folder.repository.FolderRepository;
import com.filespace.domain.folder.service.FolderService;
import com.filespace.domain.space.domain.Space;
import com.filespace.domain.space.dto.SpaceResponse;
import com.filespace.domain.space.repository.ParticipantRepository;
import com.filespace.domain.space.repository.SpaceRepository;
import com.filespace.domain.space.service.ParticipantService;
import com.filespace.domain.space.service.SpaceService;
import com.filespace.domain.user.domain.Refresh;
import com.filespace.domain.user.domain.User;
import com.filespace.domain.user.repository.RefreshRepository;
import com.filespace.domain.user.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.expression.AccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.mysql.cj.conf.PropertyKey.logger;

@Service
public class WithdrawalService {
    private final UserRepository userRepository;
    private final RefreshRepository refreshRepository;
    private final PasswordEncoder passwordEncoder;

    private final SpaceRepository spaceRepository;

    private final ParticipantRepository participantRepository;
    private final FolderService folderService;
    private final FolderRepository folderRepository;

    private final S3Service s3Service ;

    private final SpaceService spaceService;
    private final ParticipantService participantService;
    public WithdrawalService(UserRepository userRepository, PasswordEncoder passwordEncoder, RefreshRepository refreshRepository, SpaceRepository spaceRepository, SpaceService spaceService, FolderService folderService, FolderRepository folderRepository, S3Service s3Service, ParticipantService participantService, ParticipantRepository participantRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.refreshRepository = refreshRepository;
        this.spaceRepository = spaceRepository;
        this.spaceService = spaceService;
        this.folderService = folderService;
        this.folderRepository = folderRepository;
        this.s3Service = s3Service;
        this.participantService = participantService;
        this.participantRepository = participantRepository;
    }
    public void withdrawalProcess(String inputPassword) throws PasswordNotMachedException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User member = userRepository.findByEmail(email);
        Long id = member.getId();
        String url = member.getProfile_image();
        String realPassword = member.getPassword();
        if( ! passwordEncoder.matches(inputPassword, realPassword)) {
            throw new PasswordNotMachedException("비밀번호가 올바르지 않습니다");
        }


        List<SpaceResponse.SpaceInfo> spaceList =  spaceService.findAllSpace(id, true);


        for (SpaceResponse.SpaceInfo spaceInfo : spaceList) {
            Long spaceid =  spaceInfo.getSpaceId();
            List<Folder> folderList = folderRepository.findAllByFolderParentIdIsNullAndFolderDeletedAtIsNullAndSpaceId(spaceid);

            for (Folder folder : folderList){
                Long folderid = folder.getFolderId();
                try{
                    folderService.deleteFolder(folderid, id, false);
                } catch (Exception e){}
            }

            try{
                spaceService.deleteSpaceById(spaceid, id);
            }
            catch (Exception e) {continue;}

            try{
                participantService.exit(spaceid, id);
            }
            catch (Exception e) {continue;}

        }

        userRepository.delete(member);
        Refresh refresh = refreshRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Error with refresh Token"));
        refreshRepository.delete(refresh);


        s3Service.deleteFile(url);


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(null);
        }









    }
    public class PasswordNotMachedException extends Exception {
        public PasswordNotMachedException(String message) {super(message); }
    }
}
