package com.al.qdt.score.qry.infrastructure.handlers;

import com.al.qdt.common.domain.enums.Player;
import com.al.qdt.cqrs.domain.AbstractEntity;
import com.al.qdt.score.qry.api.exceptions.ScoreNotFoundException;
import com.al.qdt.score.qry.api.queries.*;
import com.al.qdt.score.qry.domain.repositories.ScoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.al.qdt.score.qry.api.exceptions.ScoreNotFoundException.*;

@Slf4j
@Service
@Transactional(readOnly = true) // for performance optimization, avoids dirty checks on all retrieved entities
@RequiredArgsConstructor
public class ScoreQueryHandler implements QueryHandler {
    private final ScoreRepository scoreRepository;

    @Override
    public List<AbstractEntity> handle(FindAllScoresQuery query) {
        log.info("Handling find all scores query.");
        final var scores = this.scoreRepository.findAll();
        if (scores.isEmpty()) {
            throw new ScoreNotFoundException(SCORES_NOT_FOUND_EXCEPTION_MESSAGE);
        }
        return new ArrayList<>(scores);
    }

    @Override
    public List<AbstractEntity> handle(FindScoreByIdQuery query) {
        final var scoreId = query.getId();
        log.info("Handling find score by id query for id: {}", scoreId.toString());
        final var score = this.scoreRepository.findById(scoreId);
        if (score.isEmpty()) {
            throw new ScoreNotFoundException(String.format(SCORE_BY_ID_NOT_FOUND_EXCEPTION_MESSAGE, scoreId));
        }
        return List.of(score.get());
    }

    @Override
    public List<AbstractEntity> handle(FindScoresByUserIdQuery query) {
        final var userId = query.getUserId();
        log.info("Handling find scores by userId query for userId: {}", userId);
        final var scores = this.scoreRepository.findByUserId(userId);
        if (scores.isEmpty()) {
            throw new ScoreNotFoundException(String.format(SCORE_BY_USER_ID_NOT_FOUND_EXCEPTION_MESSAGE, userId));
        }
        return new ArrayList<>(scores);
    }

    @Override
    public List<AbstractEntity> handle(FindScoresByWinnerQuery query) {
        final var winner = query.getWinner();
        log.info("Handling find scores by winner query for winner: {}", winner);
        final var scores = this.scoreRepository.findByWinner(Player.valueOf(winner));
        if (scores.isEmpty()) {
            throw new ScoreNotFoundException(String.format(SCORES_BY_WINNER_NOT_FOUND_EXCEPTION_MESSAGE, winner));
        }
        return new ArrayList<>(scores);
    }

    @Override
    public List<AbstractEntity> handle(FindScoresByUserIdAndWinnerQuery query) {
        final var userId = query.getUserId();
        final var winner = query.getWinner();
        log.info("Handling find scores by userId: {} and winner: {}", userId, winner);
        final var scores = this.scoreRepository.findByUserIdAndWinner(userId, Player.valueOf(winner));
        if (scores.isEmpty()) {
            throw new ScoreNotFoundException(String.format(SCORES_BY_USER_ID_AND_WINNER_NOT_FOUND_EXCEPTION_MESSAGE, userId, winner));
        }
        return new ArrayList<>(scores);
    }
}
