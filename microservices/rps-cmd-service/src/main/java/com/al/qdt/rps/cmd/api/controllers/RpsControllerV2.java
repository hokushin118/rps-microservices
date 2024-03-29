package com.al.qdt.rps.cmd.api.controllers;

import com.al.qdt.rps.cmd.api.dto.GameResponseDto;
import com.al.qdt.common.api.errors.ApiError;
import com.al.qdt.rps.cmd.api.dto.GameRequestDto;
import com.al.qdt.rps.cmd.domain.services.RpsServiceV2;
import com.al.qdt.rps.cmd.domain.services.security.AuthenticationService;
import com.al.qdt.rps.grpc.v1.dto.GameResultDto;
import com.al.qdt.rps.grpc.v1.services.GameRequest;
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

import static com.al.qdt.common.infrastructure.helpers.Constants.GAME_REQUEST_EXPECTED_JSON;
import static com.al.qdt.common.infrastructure.helpers.Constants.GAME_RESPONSE_EXPECTED_JSON;
import static com.al.qdt.common.infrastructure.helpers.Constants.HANDLER_ERROR_JSON;
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
public class RpsControllerV2 {
    private final RpsServiceV2 rpsService;
    private final AuthenticationService authenticationService;

    /**
     * Plays game.
     *
     * @param gameRequest game round user inputs, must not be null
     * @return game result
     * @version 2
     */
    @Operation(operationId = "play-proto",
            summary = "Plays game",
            description = "Plays new **RPS** game and adds result to the database.",
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
    @PostMapping("${api.version-two}/${api.endpoint-games}")
    @Timed(value = "game.play", description = "Time taken to play a round", longTask = true, percentiles = {0.5, 0.90})
    public GameResultDto play(@Parameter(description = "Game inputs that needs to be processed", required = true)
                              @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                      content = @Content(
                                              mediaType = APPLICATION_JSON_VALUE,
                                              schema = @Schema(implementation = GameRequestDto.class),
                                              examples = {
                                                      @ExampleObject(
                                                              value = GAME_REQUEST_EXPECTED_JSON
                                                      )
                                              }))
                              @Valid @NotNull @RequestBody GameRequest gameRequest) {
        log.info("REST CONTROLLER: Playing game...");
        return this.rpsService.play(gameRequest, this.getUserId());
    }

    /**
     * Plays game asynchronously.
     *
     * @param gameRequest game round user inputs, must not be null
     * @return game result
     * @version 2.1
     */
    @Operation(operationId = "play-async-proto",
            summary = "Plays game asynchronously",
            description = "Plays new **RPS** game and adds result to the database asynchronously.",
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
    @PostMapping("${api.version-two-async}/${api.endpoint-games}")
    @Timed(value = "game.play.async", description = "Time taken to play a round asynchronously", longTask = true, percentiles = {0.5, 0.90})
    public CompletableFuture<GameResultDto> playAsync(@Parameter(description = "Game inputs that needs to be processed", required = true)
                                                      @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                                              content = @Content(
                                                                      mediaType = APPLICATION_JSON_VALUE,
                                                                      schema = @Schema(implementation = GameRequestDto.class),
                                                                      examples = {
                                                                              @ExampleObject(
                                                                                      value = GAME_REQUEST_EXPECTED_JSON
                                                                              )
                                                                      }))
                                                      @Valid @NotNull @RequestBody GameRequest gameRequest) {
        log.info("REST CONTROLLER: Playing game asynchronously...");
        return this.rpsService.playAsync(gameRequest, this.getUserId());
    }

    /**
     * Deletes game by id.
     *
     * @param id game id, must not be null
     * @version 2
     */
    @Operation(operationId = "delete-by-id-proto",
            summary = "Deletes game by id",
            description = "Deletes **RPS** game from the database by its _id_. For valid response try String ids.",
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
    @DeleteMapping("${api.version-two}/${api.endpoint-admin}/${api.endpoint-games}/{id}")
    @Timed(value = "game.deleteById", description = "Time taken to delete game by id", longTask = true)
    public void deleteById(@Parameter(description = "Id of game that needs to be deleted",
            schema = @Schema(type = "string"), example = TEST_ID, required = true)
                           @Valid @NotNull @PathVariable(value = "id") UUID id) {
        log.info("REST CONTROLLER: Deleting game by id: {}.", id);
        this.rpsService.deleteById(id, this.getUserId());
    }

    /**
     * Deletes game by id asynchronously.
     *
     * @param id game id, must not be null
     * @version 2.1
     */
    @Operation(operationId = "delete-by-id-async-proto",
            summary = "Deletes game by id asynchronously",
            description = "Deletes **RPS** game from the database by its _id_ asynchronously. For valid response try String ids.",
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
    @DeleteMapping("${api.version-two-async}/${api.endpoint-admin}/${api.endpoint-games}/{id}")
    @Timed(value = "game.deleteById.async", description = "Time taken to delete game by id asynchronously", longTask = true)
    public CompletableFuture<Void> deleteByIdAsync(@Parameter(description = "Id of game that needs to be deleted",
            schema = @Schema(type = "string"), example = TEST_ID, required = true)
                                                   @Valid @NotNull @PathVariable(value = "id") UUID id) {
        log.info("REST CONTROLLER: Deleting game by id: {} asynchronously.", id);
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
