package com.al.qdt.rps.cmd.domain.services;

import com.al.qdt.rps.cmd.api.dto.GameResponseDto;
import com.al.qdt.common.domain.enums.Hand;
import com.al.qdt.cqrs.infrastructure.CommandDispatcher;
import com.al.qdt.rps.cmd.api.commands.AddScoreCommand;
import com.al.qdt.rps.cmd.api.commands.DeleteGameCommand;
import com.al.qdt.rps.cmd.api.commands.PlayGameCommand;
import com.al.qdt.rps.cmd.domain.services.base.RpsBaseService;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static com.al.qdt.common.infrastructure.config.AsyncConfig.ASYNC_TASK_EXECUTOR;

@Slf4j
@Service
public class RpsServiceV1Impl extends RpsBaseService implements RpsServiceV1 {
    private final CommandDispatcher commandDispatcher;

    public RpsServiceV1Impl(GameService gameService, MeterRegistry meterRegistry, CommandDispatcher commandDispatcher) {
        super(gameService, meterRegistry);
        this.commandDispatcher = commandDispatcher;
    }

    @Override
    public GameResponseDto play(Hand hand, UUID userId) {
        log.info("SERVICE: Playing game synchronously...");
        return this.playRound(hand, userId);
    }

    @Override
    @Async(ASYNC_TASK_EXECUTOR)
    public CompletableFuture<GameResponseDto> playAsync(Hand hand, UUID userId) {
        log.info("SERVICE: Playing game asynchronously...");
        return CompletableFuture.completedFuture(this.playRound(hand, userId));
    }

    @Override
    public void deleteById(UUID id, UUID userId) {
        log.info("SERVICE: Deleting game by id: {}.", id.toString());
        this.deleteGameById(id, userId);
    }

    @Override
    @Async(ASYNC_TASK_EXECUTOR)
    public CompletableFuture<Void> deleteByIdAsync(UUID id, UUID userId) {
        log.info("SERVICE: Deleting game by id asynchronously...");
        this.deleteGameById(id, userId);
        return CompletableFuture.completedFuture(null);
    }

    /**
     * Plays game round.
     *
     * @param hand   game round user inputs
     * @param userId user id
     * @return game result
     */
    private GameResponseDto playRound(Hand hand, UUID userId) {
        final var id = UUID.randomUUID();
        log.info("SERVICE: Playing game with id: {}", id);
        final var command = PlayGameCommand.builder()
                .id(id)
                .userId(userId)
                .hand(hand)
                .build();
        final var roundResult = playRound(hand);
        this.commandDispatcher.send(command);
        final var winner = roundResult.getWinner();
        final var addScoreCommand = AddScoreCommand.builder()
                .id(id)
                .userId(userId)
                .winner(winner)
                .build();
        this.commandDispatcher.send(addScoreCommand);
        super.updatePlayedGameMetrics();
        return GameResponseDto.builder()
                .userChoice(hand.name())
                .machineChoice(roundResult.getMachineChoice().name())
                .result(roundResult.getWinner())
                .build();
    }

    /**
     * Deletes game by id.
     *
     * @param id     game id
     * @param userId user id
     */
    private void deleteGameById(UUID id, UUID userId) {
        log.info("SERVICE: Deleting game by id: {}.", id.toString());
        this.commandDispatcher.send(DeleteGameCommand.builder()
                .id(id)
                .userId(userId)
                .build());
        super.updateDeleteGameMetrics();
    }
}
