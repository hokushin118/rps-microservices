package com.al.qdt.rps.cmd.domain.services;

import com.al.qdt.common.api.dto.GameResponseDto;
import com.al.qdt.common.domain.enums.Hand;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * This interface describes PPS game service functionality.
 */
public interface RpsServiceV1 {

    /**
     * Plays game.
     *
     * @param hand   game round user inputs
     * @param userId user id
     * @return game result
     */
    GameResponseDto play(Hand hand, UUID userId);

    /**
     * Plays game asynchronously.
     *
     * @param hand   game round user inputs
     * @param userId user id
     * @return game result
     */
    CompletableFuture<GameResponseDto> playAsync(Hand hand, UUID userId);

    /**
     * Deletes game by id.
     *
     * @param id     game id
     * @param userId user id
     */
    void deleteById(UUID id, UUID userId);

    /**
     * Deletes game by id asynchronously.
     *
     * @param id     game id
     * @param userId user id
     */
    CompletableFuture<Void> deleteByIdAsync(UUID id, UUID userId);
}
