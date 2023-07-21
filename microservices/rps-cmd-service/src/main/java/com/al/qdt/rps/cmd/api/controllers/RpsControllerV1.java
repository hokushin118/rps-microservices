package com.al.qdt.rps.cmd.api.controllers;

import com.al.qdt.rps.cmd.api.dto.GameResponseDto;
import com.al.qdt.common.api.errors.ApiError;
import com.al.qdt.common.domain.enums.Hand;
import com.al.qdt.rps.cmd.domain.services.RpsServiceV1;
import com.al.qdt.rps.cmd.domain.services.security.AuthenticationService;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static com.al.qdt.common.infrastructure.helpers.Constants.GAME_RESPONSE_EXPECTED_JSON;
import static com.al.qdt.common.infrastructure.helpers.Constants.HANDLER_ERROR_JSON;
import static com.al.qdt.common.infrastructure.helpers.Constants.HAND_EXAMPLE;
import static com.al.qdt.common.infrastructure.helpers.Constants.INCORRECT_ID_JSON;
import static com.al.qdt.common.infrastructure.helpers.Constants.MALFORMED_JSON;
import static com.al.qdt.common.infrastructure.helpers.Constants.MULTIPLE_HANDLERS_ERROR_JSON;
import static com.al.qdt.common.infrastructure.helpers.Constants.TEST_ID;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Controller for managing game rounds.
 */
@Slf4j(topic = "outbound-logs")
@RestController
@RequestMapping(produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Validated
@Timed("game") // enables timings on every request handler in the controller
@Tag(name = "Game", description = "the game command REST API endpoints")
public class RpsControllerV1 {
    private final RpsServiceV1 rpsService;
    private final AuthenticationService authenticationService;

    /**
     * Plays game.
     *
     * @param hand game round user inputs, must not be null
     * @return game result
     * @version 1
     * @deprecated <p>
     * This method is deprecated.
     */
    @Operation(operationId = "play-json",
            summary = "Plays game",
            description = "Plays new **RPS** game and adds result to the database.",
            deprecated = true,
            tags = {"game"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Successful operation",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = GameResponseDto.class),
                            examples = {
                                    @ExampleObject(
                                            value = GAME_RESPONSE_EXPECTED_JSON
                                    )
                            })),
            @ApiResponse(responseCode = "400",
                    description = "Invalid request",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiError.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Invalid game inputs supplied",
                                            value = MALFORMED_JSON
                                    ),
                                    @ExampleObject(
                                            name = "No command handler",
                                            value = HANDLER_ERROR_JSON
                                    ),
                                    @ExampleObject(
                                            name = "Multiple command handlers",
                                            value = MULTIPLE_HANDLERS_ERROR_JSON
                                    )}
                    ))
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("${api.version-one}/${api.endpoint-games}")
    @Timed(value = "game.play",
            description = "Time taken to play a round",
            longTask = true, // long task timers require a separate metric name
            percentiles = {0.5, 0.90})
    public GameResponseDto play(@Parameter(description = "Game inputs that needs to be processed", required = true)
                                @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                        content = @Content(
                                                mediaType = APPLICATION_JSON_VALUE,
                                                schema = @Schema(implementation = Hand.class),
                                                examples = {
                                                        @ExampleObject(
                                                                value = HAND_EXAMPLE
                                                        )
                                                }))
                                @Valid @NotNull @RequestBody Hand hand) {
        log.info("REST CONTROLLER: Playing game...");
        return this.rpsService.play(hand, this.getUserId());
    }

    /**
     * Plays game asynchronously.
     *
     * @param hand game round user inputs, must not be null
     * @return game result
     * @version 1.1
     * @deprecated <p>
     * This method is deprecated.
     */
    @Operation(operationId = "play-async-json",
            summary = "Plays game asynchronously",
            description = "Plays new **RPS** game and adds result to the database asynchronously.",
            deprecated = true,
            tags = {"game-async"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Successful operation",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = GameResponseDto.class),
                            examples = {
                                    @ExampleObject(
                                            value = GAME_RESPONSE_EXPECTED_JSON
                                    )
                            })),
            @ApiResponse(responseCode = "400",
                    description = "Invalid request",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiError.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Invalid game inputs supplied",
                                            value = MALFORMED_JSON
                                    ),
                                    @ExampleObject(
                                            name = "No command handler",
                                            value = HANDLER_ERROR_JSON
                                    ),
                                    @ExampleObject(
                                            name = "Multiple command handlers",
                                            value = MULTIPLE_HANDLERS_ERROR_JSON
                                    )}
                    ))
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("${api.version-one-async}/${api.endpoint-games}")
    @Timed(value = "game.play.async",
            description = "Time taken to play a round asynchronously",
            longTask = true, // long task timers require a separate metric name
            percentiles = {0.5, 0.90})
    public CompletableFuture<GameResponseDto> playAsync(@Parameter(description = "Game inputs that needs to be processed", required = true)
                                                        @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                                                content = @Content(
                                                                        mediaType = APPLICATION_JSON_VALUE,
                                                                        schema = @Schema(implementation = Hand.class),
                                                                        examples = {
                                                                                @ExampleObject(
                                                                                        value = HAND_EXAMPLE
                                                                                )
                                                                        }))
                                                        @Valid @NotNull @RequestBody Hand hand) {
        log.info("REST CONTROLLER: Playing game asynchronously...");
        return this.rpsService.playAsync(hand, this.getUserId());
    }

    /**
     * Deletes game by id.
     *
     * @param id game id, must not be null or empty
     * @version 1
     * @deprecated <p>
     * This method is deprecated.
     */
    @Operation(operationId = "delete-by-id-json",
            summary = "Deletes game by id",
            description = "Deletes **RPS** game from the database by its _id_. For valid response try String ids.",
            deprecated = true,
            tags = {"game"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Successful operation",
                    content = @Content),
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
            @ApiResponse(responseCode = "400",
                    description = "Invalid request",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiError.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Invalid game id supplied",
                                            value = MALFORMED_JSON
                                    ),
                                    @ExampleObject(
                                            name = "No command handler",
                                            value = HANDLER_ERROR_JSON
                                    ),
                                    @ExampleObject(
                                            name = "Multiple command handlers",
                                            value = MULTIPLE_HANDLERS_ERROR_JSON
                                    )}
                    )),
            @ApiResponse(responseCode = "404",
                    description = "Incorrect game id",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiError.class, example = INCORRECT_ID_JSON)
                    ))
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("${api.version-one}/${api.endpoint-admin}/${api.endpoint-games}/{id}")
    @Timed(value = "game.deleteById", description = "Time taken to delete game by id", longTask = true)
    public void deleteById(@Parameter(description = "Id of game that needs to be deleted", example = TEST_ID, required = true)
                           @Valid @NotNull @PathVariable(value = "id") UUID id) {
        log.info("REST CONTROLLER: Deleting game by id: {}.", id.toString());
        this.rpsService.deleteById(id, this.getUserId());
    }

    /**
     * Deletes game by id asynchronously.
     *
     * @param id game id, must not be null or empty
     * @version 1.1
     * @deprecated <p>
     * This method is deprecated.
     */
    @Operation(operationId = "delete-by-id-async-json",
            summary = "Deletes game by id asynchronously",
            description = "Deletes **RPS** game from the database by its _id_ asynchronously. For valid response try String ids.",
            deprecated = true,
            tags = {"game-async"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Successful operation",
                    content = @Content),
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
            @ApiResponse(responseCode = "400",
                    description = "Invalid request",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiError.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Invalid game id supplied",
                                            value = MALFORMED_JSON
                                    ),
                                    @ExampleObject(
                                            name = "No command handler",
                                            value = HANDLER_ERROR_JSON
                                    ),
                                    @ExampleObject(
                                            name = "Multiple command handlers",
                                            value = MULTIPLE_HANDLERS_ERROR_JSON
                                    )}
                    )),
            @ApiResponse(responseCode = "404",
                    description = "Incorrect game id",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiError.class, example = INCORRECT_ID_JSON)
                    ))
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("${api.version-one-async}/${api.endpoint-admin}/${api.endpoint-games}/{id}")
    @Timed(value = "game.deleteById.async", description = "Time taken to delete game by id asynchronously", longTask = true)
    public CompletableFuture<Void> deleteByIdAsync(@Parameter(description = "Id of game that needs to be deleted", example = TEST_ID, required = true)
                                                   @Valid @NotNull @PathVariable(value = "id") UUID id) {
        log.info("REST CONTROLLER: Deleting game by id: {} asynchronously.", id.toString());
        return this.rpsService.deleteByIdAsync(id, this.getUserId());
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
