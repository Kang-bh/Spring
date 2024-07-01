package com.filespace.domain.space.controller;

import com.filespace.domain.space.domain.Participant;
import com.filespace.domain.space.domain.Space;
import com.filespace.domain.space.dto.ParticipantRequest;
import com.filespace.domain.space.dto.ParticipantResponse;
import com.filespace.domain.space.service.ParticipantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@Tag(name = "Participant", description = "Participant API")
@Validated
public class ParticipantController {

    // todo : study why required final
    private final ParticipantService participantService;

    @Operation(summary = "enterSpace", description = "use when enter space")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = Space.class)))
                    ),
                    @ApiResponse(responseCode = "400", description = "Bad Requests",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "No Space existing",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            })
    @PostMapping("/api/spaces/{spaceId}/enter")
    public ResponseEntity<Void> enterSpace(
            @RequestBody Optional<ParticipantRequest.EnterSpace> body,
            @PathVariable("spaceId") long spaceId,
            @RequestParam(name = "userId", required = false) Long userId
    ) {

        // todo : user 검증
        // todo : 이미 존재하는지 확인


        participantService.save(spaceId, userId, body);

        // todo : data 꼴 합의
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();

    }

    // todo : check method
    @Operation(summary = "exitSpace", description = "use this when exit space")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = Space.class)))
                    ),
                    @ApiResponse(responseCode = "400", description = "Bad Requests",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "No Space existing",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            })
    @PostMapping("/api/spaces/{spaceId}/exit")
    public ResponseEntity<Void> exitSpace(
            @PathVariable("spaceId") long spaceId,
            @RequestParam(name = "userId", required = false) Long userId
    ) {
        // owner 면 space 삭제
        // todo: socket or redis or kafka 통해서 삭제된 것 알림
        // todo : user check

        participantService.exit(spaceId, userId);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
    @Operation(summary = "get all participants", description = "use this when get participant list by spaceId")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = Space.class)))
                    ),
                    @ApiResponse(responseCode = "400", description = "Bad Requests",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "No Space existing",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            })
    @GetMapping("/api/spaces/{spaceId}/participants")
    public ResponseEntity<List<ParticipantResponse.ParticipantInfo>> findAllParticipants(
            @PathVariable("spaceId") long spaceId
    ) {
        List<ParticipantResponse.ParticipantInfo> participantList = participantService.findAllParticipants(spaceId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

//    @GetMapping("/api/spaces/{spaceId}/participants/{participantId}")
//    public ResponseEntity<ParticipantResponse.ParticipantInfo> findParticipant(
//            @PathVariable("spaceId") long spaceId
//    ) {
//        List<ParticipantResponse.ParticipantInfo> participantList = participantService.findAllParticipants(spaceId);
//
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .build();
//    }


}
