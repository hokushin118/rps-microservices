package com.al.qdt.rps.cmd.domain.services;

import com.al.qdt.common.api.dto.BaseResponseDto;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * This interface describes database service functionality.
 */
public interface DbServiceV1 {

    /**
     * Restore database.
     *
     * @param userId user id
     * @return operation result
     */
    BaseResponseDto restoreDb(UUID userId);

    /**
     * Restore database asynchronously.
     *
     * @param userId user id
     * @return operation result
     */
    CompletableFuture<BaseResponseDto> restoreDbAsync(UUID userId);
}
