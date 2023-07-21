package com.al.qdt.rps.cmd.domain.services;

import com.al.qdt.cqrs.infrastructure.CommandDispatcher;
import com.al.qdt.rps.cmd.api.commands.RestoreDbCommand;
import com.al.qdt.rps.cmd.api.dto.BaseResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static com.al.qdt.common.infrastructure.helpers.Constants.SUCCESS_MESSAGE;
import static com.al.qdt.common.infrastructure.config.AsyncConfig.ASYNC_TASK_EXECUTOR;

@Slf4j
@Service
@RequiredArgsConstructor
public class DbServiceV1Impl implements DbServiceV1 {
    private final CommandDispatcher commandDispatcher;

    @Override
    public BaseResponseDto restoreDb(UUID userId) {
        log.info("SERVICE: Restoring the database synchronously...");
        return this.restoreRpsDb(userId);
    }

    @Override
    @Async(ASYNC_TASK_EXECUTOR)
    public CompletableFuture<BaseResponseDto> restoreDbAsync(UUID userId) {
        log.info("SERVICE: Restoring the database asynchronously...");
        return CompletableFuture.completedFuture(this.restoreRpsDb(userId));
    }

    /**
     * Restore database.
     *
     * @param userId user id
     * @return operation result
     */
    private BaseResponseDto restoreRpsDb(UUID userId) {
        this.commandDispatcher.send(RestoreDbCommand.builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .build());
        return BaseResponseDto.builder()
                .message(SUCCESS_MESSAGE)
                .build();
    }
}
