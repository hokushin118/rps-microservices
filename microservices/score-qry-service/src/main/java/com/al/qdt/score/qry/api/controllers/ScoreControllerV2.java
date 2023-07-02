package com.al.qdt.score.qry.api.controllers;

import com.al.qdt.common.api.errors.ApiError;
import com.al.qdt.rps.grpc.v1.common.Player;
import com.al.qdt.rps.grpc.v1.dto.ScoreAdminDto;
import com.al.qdt.rps.grpc.v1.dto.ScoreDto;
import com.al.qdt.rps.grpc.v1.services.ListOfScoresAdminResponse;
import com.al.qdt.rps.grpc.v1.services.ListOfScoresResponse;
import com.al.qdt.score.qry.domain.services.ScoreServiceV2;
import com.al.qdt.score.qry.domain.services.security.AuthenticationService;
import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

import static com.al.qdt.common.infrastructure.helpers.Constants.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Controller for managing game scores.
 */
@Slf4j(topic = "outbound-logs")
@RestController
@RequestMapping(path = "${api.version-two}", produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Validated
@Timed("score")
@Tag(name = "Score", description = "the score query REST API endpoints")
public class ScoreControllerV2 {
    private final ScoreServiceV2 scoreService;
    private final AuthenticationService authenticationService;

    /**
     * Returns all scores.
     *
     * @return paged list of scores
     * @version 2
     */
    @Operation(operationId = "all-proto",
            summary = "Returns all scores",
            description = "Returns all scores from the database.",
            tags = {"score"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successful operation",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = ScoreAdminDto.class)),
                            examples = {
                                    @ExampleObject(
                                            value = SCORES_ADMIN_EXPECTED_JSON
                                    )
                            })),
            @ApiResponse(responseCode = "404",
                    description = "Scores not found",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiError.class),
                            examples = {
                                    @ExampleObject(
                                            value = SCORES_NOT_FOUND_JSON
                                    )
                            }
                    ))
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/${api.endpoint-admin}/${api.endpoint-scores}")
    @Timed(value = "score.all", description = "Time taken to return all scores", longTask = true)
    public ListOfScoresAdminResponse all(@Parameter(description = "User id of games that need to be fetched", example = USER_ONE_ID_EXAMPLE)
                                    @Valid @RequestParam(value = "userId", required = false) UUID userId,
                                         @Parameter(description = "Winner of scores that need to be fetched", schema = @Schema(type = "string", allowableValues = {"USER", "DRAW", "MACHINE"}), example = TEST_WINNER)
                                    @Valid @RequestParam(value = "winner", required = false) Player player) {
        log.info("REST CONTROLLER: Getting all scores.");
        if (userId != null && player != null) {
            return this.scoreService.findByUserIdAndWinner(userId, player);
        }
        if (userId != null) {
            return this.scoreService.findByUserId(userId);
        }
        if (player != null) {
            return this.scoreService.findByWinner(player);
        }
        return this.scoreService.all();
    }

    /**
     * Find scores by id.
     *
     * @param id score id, must not be null or empty
     * @return found score
     * @version 2
     */
    @Operation(operationId = "find-by-id-proto",
            summary = "Find scores by id",
            description = "Find a score in the database by its id. For valid response try String ids.",
            tags = {"score"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successful operation",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ScoreAdminDto.class),
                            examples = {
                                    @ExampleObject(
                                            value = SCORE_ADMIN_EXPECTED_JSON
                                    )
                            })),
            @ApiResponse(responseCode = "400",
                    description = "Invalid id supplied",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiError.class),
                            examples = {
                                    @ExampleObject(
                                            value = MALFORMED_JSON
                                    )
                            }
                    )),
            @ApiResponse(responseCode = "404",
                    description = "Scores not found",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiError.class),
                            examples = {
                                    @ExampleObject(
                                            value = SCORE_BY_ID_NOT_FOUND_JSON
                                    )
                            }
                    ))
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/${api.endpoint-admin}/${api.endpoint-scores}/{id}")
    @Timed(value = "score.findById", description = "Time taken to find a score by its id", longTask = true)
    public ScoreAdminDto findById(@Parameter(description = "Id of score that needs to be fetched",
            schema = @Schema(type = "string"), example = TEST_ID, required = true)
                             @Valid @NotNull @PathVariable(value = "id") UUID id) {
        log.info("REST CONTROLLER: Finding scores by id: {}.", id.toString());
        return this.scoreService.findById(id);
    }

    /**
     * Find my scores.
     *
     * @return found collection of scores
     * @version 2
     */
    @Operation(operationId = "find-my-scores-proto",
            summary = "Find my scores",
            description = "Find my scores in the database.",
            tags = {"score"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successful operation",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = ScoreDto.class)),
                            examples = {
                                    @ExampleObject(
                                            value = SCORES_EXPECTED_JSON
                                    )
                            })),
            @ApiResponse(responseCode = "400",
                    description = "Invalid winner supplied",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiError.class),
                            examples = {
                                    @ExampleObject(
                                            value = MALFORMED_JSON
                                    )
                            }
                    )),
            @ApiResponse(responseCode = "404",
                    description = "Scores not found",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiError.class),
                            examples = {
                                    @ExampleObject(
                                            value = SCORES_BY_USER_ID_NOT_FOUND_JSON
                                    )
                            }
                    ))
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/${api.endpoint-scores}")
    @Timed(value = "score.findMyScores", description = "Time taken to find my scores", longTask = true)
    public ListOfScoresResponse findMyScores() {
        final var userId = this.getUserId();
        log.info("REST CONTROLLER: Finding scores by my user id: {}.", userId);
        return this.scoreService.findMyScores(userId);
    }

    /**
     * Returns currently logged in user id.
     *
     * @return user id
     */
    private UUID getUserId() {
        return this.authenticationService.getUserId();
    }
}
