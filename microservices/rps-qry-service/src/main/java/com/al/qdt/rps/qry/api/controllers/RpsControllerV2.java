package com.al.qdt.rps.qry.api.controllers;

import com.al.qdt.common.api.errors.ApiError;
import com.al.qdt.rps.grpc.v1.common.SortingOrder;
import com.al.qdt.rps.grpc.v1.dto.GameAdminDto;
import com.al.qdt.rps.grpc.v1.dto.GameDto;
import com.al.qdt.rps.grpc.v1.services.ListOfGamesAdminResponse;
import com.al.qdt.rps.grpc.v1.services.ListOfGamesResponse;
import com.al.qdt.rps.qry.api.dto.GameAdminPagedResponseDto;
import com.al.qdt.rps.qry.api.dto.GamePagedResponseDto;
import com.al.qdt.rps.qry.domain.services.RpsServiceV2;
import com.al.qdt.rps.qry.domain.services.security.AuthenticationService;
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

import static com.al.qdt.common.infrastructure.helpers.Constants.GAMES_ADMIN_EXPECTED_JSON;
import static com.al.qdt.common.infrastructure.helpers.Constants.GAMES_BY_USER_NOT_FOUND_JSON;
import static com.al.qdt.common.infrastructure.helpers.Constants.GAMES_EXPECTED_JSON;
import static com.al.qdt.common.infrastructure.helpers.Constants.GAMES_NOT_FOUND_JSON;
import static com.al.qdt.common.infrastructure.helpers.Constants.GAME_ADMIN_EXPECTED_JSON;
import static com.al.qdt.common.infrastructure.helpers.Constants.GAME_BY_ID_NOT_FOUND_JSON;
import static com.al.qdt.common.infrastructure.helpers.Constants.MALFORMED_JSON;
import static com.al.qdt.common.infrastructure.helpers.Constants.TEST_ID;
import static com.al.qdt.common.infrastructure.helpers.Constants.USER_ONE_ID_EXAMPLE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Controller for managing game rounds.
 */
@Slf4j(topic = "outbound-logs")
@RestController
@RequestMapping(path = "${api.version-two}", produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Validated
@Timed("game")
@Tag(name = "Game", description = "the rock paper scissors game query REST API endpoints")
public class RpsControllerV2 {
    private final RpsServiceV2 rpsService;
    private final AuthenticationService authenticationService;

    /**
     * Returns all games with pagination.
     * <p>
     * GET
     * /admin/games?currentPage=1
     * /admin/games?currentPage=1&pageSize=10
     * /admin/games?currentPage=1&pageSize=10&sortBy=id
     * /admin/games?currentPage=1&pageSize=10&sortBy=id&sortingOrder=asc
     *
     * @param userId       user id
     * @param currentPage  current page
     * @param pageSize     page size
     * @param sortBy       sorting field
     * @param sortingOrder sorting order
     * @return collection of games
     * @version 2
     */
    @Operation(operationId = "all-proto",
            summary = "Returns all games",
            description = "Returns all games from the database.",
            tags = {"game"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successful operation",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = GameAdminPagedResponseDto.class),
                            examples = {
                                    @ExampleObject(
                                            value = GAMES_ADMIN_EXPECTED_JSON
                                    )
                            })),
            @ApiResponse(responseCode = "404",
                    description = "Games not found",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiError.class),
                            examples = {
                                    @ExampleObject(
                                            value = GAMES_NOT_FOUND_JSON
                                    )
                            }
                    ))
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/${api.endpoint-admin}/${api.endpoint-games}")
    @Timed(value = "game.all", description = "Time taken to return all games", longTask = true)
    public ListOfGamesAdminResponse all(@Parameter(description = "User id of games that need to be fetched", example = USER_ONE_ID_EXAMPLE)
                                        @Valid @RequestParam(value = "userId", required = false) UUID userId,
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
        log.info("REST CONTROLLER: Getting all games...");
        return userId == null ? this.rpsService.all(currentPage, pageSize, sortBy, sortingOrder) : this.rpsService.findByUserId(userId, currentPage, pageSize, sortBy, sortingOrder);
    }

    /**
     * Find game by id.
     * <p>
     * GET
     * /admin/games/550e8400-e29b-41d4-a716-446655440000
     *
     * @param id game id, must not be null or empty
     * @return found game
     * @version 2
     */
    @Operation(operationId = "find-by-id-proto",
            summary = "Find game by id",
            description = "Find a game in the database by its id. For valid response try String ids.",
            tags = {"game"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successful operation",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = GameAdminDto.class),
                            examples = {
                                    @ExampleObject(
                                            value = GAME_ADMIN_EXPECTED_JSON
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
                    description = "Games not found",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiError.class),
                            examples = {
                                    @ExampleObject(
                                            value = GAME_BY_ID_NOT_FOUND_JSON
                                    )
                            }
                    ))
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/${api.endpoint-admin}/${api.endpoint-games}/{id}")
    @Timed(value = "game.findById", description = "Time taken to find game by id", longTask = true)
    public GameAdminDto findById(@Parameter(description = "Id of game that needs to be fetched",
            schema = @Schema(type = "string"), example = TEST_ID, required = true)
                                 @Valid @NotNull @PathVariable(value = "id") UUID id) {
        log.info("REST CONTROLLER: Finding game by id: {}.", id.toString());
        return this.rpsService.findById(id);
    }

    /**
     * Find all my games.
     * <p>
     * GET
     * /games?currentPage=1
     * /games?currentPage=1&pageSize=10
     * /games?currentPage=1&pageSize=10&sortBy=id
     * /games?currentPage=1&pageSize=10&sortBy=id&sortingOrder=asc
     *
     * @param currentPage  current page
     * @param pageSize     page size
     * @param sortBy       sorting field
     * @param sortingOrder sorting order
     * @return found collection of games
     * @version 2
     */
    @Operation(operationId = "find-my-games-proto",
            summary = "Find my games",
            description = "Find my games in the database.",
            tags = {"game"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successful operation",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = GamePagedResponseDto.class),
                            examples = {
                                    @ExampleObject(
                                            value = GAMES_EXPECTED_JSON
                                    )
                            })),
            @ApiResponse(responseCode = "400",
                    description = "Invalid userId supplied",
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
                    description = "Games not found",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiError.class),
                            examples = {
                                    @ExampleObject(
                                            value = GAMES_BY_USER_NOT_FOUND_JSON
                                    )
                            }
                    ))
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/${api.endpoint-games}")
    @Timed(value = "game.findMyGames", description = "Time taken to find my games", longTask = true)
    public ListOfGamesResponse findMyGames(@Parameter(description = "Current page", example = "1")
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
        log.info("REST CONTROLLER: Finding game by my user id: {}.", userId);
        return this.rpsService.findMyGames(userId, currentPage, pageSize, sortBy, sortingOrder);
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
