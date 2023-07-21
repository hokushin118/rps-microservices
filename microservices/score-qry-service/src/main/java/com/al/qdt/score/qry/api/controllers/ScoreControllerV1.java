package com.al.qdt.score.qry.api.controllers;

import com.al.qdt.cqrs.queries.SortingOrder;
import com.al.qdt.score.qry.api.dto.ScoreAdminDto;
import com.al.qdt.score.qry.api.dto.ScoreAdminPagedResponseDto;
import com.al.qdt.common.api.errors.ApiError;
import com.al.qdt.common.domain.enums.Player;
import com.al.qdt.score.qry.api.dto.ScorePagedResponseDto;
import com.al.qdt.score.qry.domain.services.ScoreServiceV1;
import com.al.qdt.score.qry.domain.services.security.AuthenticationService;
import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

import static com.al.qdt.common.infrastructure.helpers.Constants.MALFORMED_JSON;
import static com.al.qdt.common.infrastructure.helpers.Constants.SCORES_ADMIN_EXPECTED_JSON;
import static com.al.qdt.common.infrastructure.helpers.Constants.SCORES_BY_USER_ID_NOT_FOUND_JSON;
import static com.al.qdt.common.infrastructure.helpers.Constants.SCORES_EXPECTED_JSON;
import static com.al.qdt.common.infrastructure.helpers.Constants.SCORES_NOT_FOUND_JSON;
import static com.al.qdt.common.infrastructure.helpers.Constants.SCORE_ADMIN_EXPECTED_JSON;
import static com.al.qdt.common.infrastructure.helpers.Constants.SCORE_BY_ID_NOT_FOUND_JSON;
import static com.al.qdt.common.infrastructure.helpers.Constants.TEST_ID;
import static com.al.qdt.common.infrastructure.helpers.Constants.TEST_WINNER;
import static com.al.qdt.common.infrastructure.helpers.Constants.USER_ONE_ID_EXAMPLE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Controller for managing game scores.
 */
@Slf4j(topic = "outbound-logs")
@RestController
@RequestMapping(path = "${api.version-one}", produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Validated
@Timed("score")
@Tag(name = "Score", description = "the score query REST API endpoints")
public class ScoreControllerV1 {
    private final ScoreServiceV1 scoreService;
    private final AuthenticationService authenticationService;

    /**
     * Returns all scores with pagination.
     * <p>
     * GET
     * /admin/scores?currentPage=1
     * /admin/scores?currentPage=1&pageSize=10
     * /admin/scores?currentPage=1&pageSize=10&sortBy=id
     * /admin/scores?currentPage=1&pageSize=10&sortBy=id&sortingOrder=asc
     *
     * @param userId       user id
     * @param player       winner
     * @param currentPage  current page
     * @param pageSize     page size
     * @param sortBy       sorting field
     * @param sortingOrder sorting order
     * @return collection of scores
     * @version 1
     * @deprecated <p>
     * This method is deprecated.
     */
    @Operation(operationId = "all-json",
            summary = "Returns all scores",
            description = "Returns all scores from the database.",
            deprecated = true,
            tags = {"score"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successful operation",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ScoreAdminPagedResponseDto.class),
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
    public ScoreAdminPagedResponseDto all(@Parameter(description = "User id of games that need to be fetched", example = USER_ONE_ID_EXAMPLE)
                                          @Valid @RequestParam(value = "userId", required = false) UUID userId,
                                          @Parameter(description = "Winner of scores that need to be fetched", example = TEST_WINNER)
                                          @Valid @RequestParam(value = "winner", required = false) Player player,
                                          @Parameter(description = "Current page", example = "1")
                                          @RequestParam(value = "currentPage", defaultValue = "${api.default-page-number}", required = false) int currentPage,
                                          @Parameter(description = "Page size", example = "10")
                                          @RequestParam(value = "pageSize", defaultValue = "${api.default-page-size}", required = false) int pageSize,
                                          @Parameter(description = "Sorting field", example = "id")
                                          @RequestParam(value = "sortBy", defaultValue = "${api.default-sort-by}", required = false) String sortBy,
                                          @Parameter(description = "Sorting order",
                                                  schema = @Schema(type = "string", allowableValues = {"ASC", "DESC"}),
                                                  example = "ASC")
                                          @RequestParam(value = "sortingOrder", defaultValue = "${api.default-sort-order}", required = false) SortingOrder sortingOrder) {
        log.info("REST CONTROLLER: Getting all scores.");
        if (userId != null && player != null) {
            return this.scoreService.findByUserIdAndWinner(userId, player, currentPage, pageSize, sortBy, sortingOrder);
        }
        if (userId != null) {
            return this.scoreService.findByUserId(userId, currentPage, pageSize, sortBy, sortingOrder);
        }
        if (player != null) {
            return this.scoreService.findByWinner(player, currentPage, pageSize, sortBy, sortingOrder);
        }
        return this.scoreService.all(currentPage, pageSize, sortBy, sortingOrder);
    }

    /**
     * Find scores by id.
     *
     * <p>
     * GET
     * /admin/scores/550e8400-e29b-41d4-a716-446655440000
     *
     * @param id score id, must not be null or empty
     * @return found score
     * @version 1
     * @deprecated <p>
     * This method is deprecated.
     */
    @Operation(operationId = "find-by-id-json",
            summary = "Find scores by id",
            description = "Find a score in the database by its id. For valid response try String ids.",
            deprecated = true,
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
    public ScoreAdminDto findById(@Parameter(description = "Id of score that needs to be fetched", example = TEST_ID, required = true)
                                  @Valid @NotNull @PathVariable(value = "id") UUID id) {
        log.info("REST CONTROLLER: Finding scores by id: {}.", id.toString());
        return this.scoreService.findById(id);
    }

    /**
     * Find my scores with pagination.
     * <p>
     * GET
     * /scores?currentPage=1
     * /scores?currentPage=1&pageSize=10
     * /scores?currentPage=1&pageSize=10&sortBy=id
     * /scores?currentPage=1&pageSize=10&sortBy=id&sortingOrder=asc
     *
     * @param currentPage  current page
     * @param pageSize     page size
     * @param sortBy       sorting field
     * @param sortingOrder sorting order
     * @return found collection of scores
     * @version 1
     * @deprecated <p>
     * This method is deprecated.
     */
    @Operation(operationId = "find-my-scores-json",
            summary = "Find my scores",
            description = "Find my scores in the database.",
            deprecated = true,
            tags = {"score"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successful operation",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ScorePagedResponseDto.class),
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
    public ScorePagedResponseDto findMyScores(@Parameter(description = "Current page", example = "1")
                                              @RequestParam(value = "currentPage", defaultValue = "${api.default-page-number}", required = false) int currentPage,
                                              @Parameter(description = "Page size", example = "10")
                                              @RequestParam(value = "pageSize", defaultValue = "${api.default-page-size}", required = false) int pageSize,
                                              @Parameter(description = "Sorting field", example = "id")
                                              @RequestParam(value = "sortBy", defaultValue = "${api.default-sort-by}", required = false) String sortBy,
                                              @Parameter(description = "Sorting order",
                                                      schema = @Schema(type = "string", allowableValues = {"ASC", "DESC"}),
                                                      example = "ASC")
                                              @RequestParam(value = "sortingOrder", defaultValue = "${api.default-sort-order}", required = false) SortingOrder sortingOrder) {
        final var userId = this.getUserId();
        log.info("REST CONTROLLER: Finding scores by my user id: {}.", userId);
        return this.scoreService.findMyScores(userId, currentPage, pageSize, sortBy, sortingOrder);
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
