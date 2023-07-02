package com.al.qdt.rps.cmd.api.controllers;

import com.al.qdt.common.api.errors.ApiError;
import com.al.qdt.rps.cmd.domain.services.DbServiceV2;
import com.al.qdt.rps.cmd.domain.services.security.AuthenticationService;
import com.al.qdt.rps.grpc.v1.common.BaseResponseDto;
import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static com.al.qdt.common.infrastructure.helpers.Constants.BASE_RESPONSE_EXPECTED_JSON;
import static com.al.qdt.common.infrastructure.helpers.Constants.HANDLER_ERROR_JSON;
import static com.al.qdt.common.infrastructure.helpers.Constants.MULTIPLE_HANDLERS_ERROR_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Controller for managing database.
 */
@Slf4j(topic = "outbound-logs")
@RestController
@RequestMapping
@RequiredArgsConstructor
@Timed("database")
@Tag(name = "Database", description = "the database command REST API endpoints")
public class DbControllerV2 {
    private final DbServiceV2 dbService;
    private final AuthenticationService authenticationService;

    /**
     * Restoring database.
     *
     * @return operation result
     * @version 2
     */
    @Operation(operationId = "restore-db-proto",
            summary = "Restores database",
            description = "Restores database from event storage.",
            tags = {"database"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Successful operation",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BaseResponseDto.class),
                            examples = {
                                    @ExampleObject(
                                            value = BASE_RESPONSE_EXPECTED_JSON
                                    )
                            })),
            @ApiResponse(responseCode = "400",
                    description = "Invalid request",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiError.class),
                            examples = {
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
    @PostMapping("${api.version-two}/${api.endpoint-admin}/${api.endpoint-db}/${api.endpoint-db-restore}")
    @Timed(value = "database", description = "Time taken to restore database", longTask = true)
    public BaseResponseDto restoreReadDb() {
        log.info("REST CONTROLLER: Restoring database...");
        return this.dbService.restoreDb(this.getUserId());
    }

    /**
     * Restoring database asynchronously.
     *
     * @return operation result
     * @version 2.1
     */
    @Operation(operationId = "restore-db-async-proto",
            summary = "Restores database asynchronously",
            description = "Restores database from event storage asynchronously.",
            tags = {"database-async"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Successful operation",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BaseResponseDto.class),
                            examples = {
                                    @ExampleObject(
                                            value = BASE_RESPONSE_EXPECTED_JSON
                                    )
                            })),
            @ApiResponse(responseCode = "400",
                    description = "Invalid request",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiError.class),
                            examples = {
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
    @PostMapping("${api.version-two-async}/${api.endpoint-admin}/${api.endpoint-db}/${api.endpoint-db-restore}")
    @Timed(value = "database.async", description = "Time taken to restore database asynchronously", longTask = true)
    public CompletableFuture<BaseResponseDto> restoreReadDbAsync() {
        log.info("REST CONTROLLER: Restoring database asynchronously...");
        return this.dbService.restoreDbAsync(this.getUserId());
    }

    /**
     * Returns currently logged in user id.
     * @return user id
     */
    private UUID getUserId(){
        return this.authenticationService.getUserId();
    }
}
