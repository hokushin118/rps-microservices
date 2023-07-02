package com.al.qdt.rps.cmd.domain.services;

import com.al.qdt.rps.grpc.v1.dto.GameResultDto;
import com.al.qdt.rps.grpc.v1.services.GameRequest;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * This interface describes RPS game service functionality.
 */
public interface RpsServiceV2 {

    /**
     * Plays game.
     *
     * @param gameRequest game round user inputs
     * @param userId      user id
     * @return game result
     */
    GameResultDto play(GameRequest gameRequest, UUID userId);

    /**
     * Plays game asynchronously.
     *
     * @param gameRequest game round user inputs
     * @param userId      user id
     * @return game result
     */
    CompletableFuture<GameResultDto> playAsync(GameRequest gameRequest, UUID userId);

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
