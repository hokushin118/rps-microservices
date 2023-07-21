package com.al.qdt.score.qry.infrastructure.handlers;

import com.al.qdt.common.domain.enums.Player;
import com.al.qdt.cqrs.domain.AbstractEntity;
import com.al.qdt.score.qry.api.exceptions.ScoreNotFoundException;
import com.al.qdt.score.qry.api.queries.FindAllScoresQuery;
import com.al.qdt.score.qry.api.queries.FindScoreByIdQuery;
import com.al.qdt.score.qry.api.queries.FindScoresByUserIdAndWinnerQuery;
import com.al.qdt.score.qry.api.queries.FindScoresByUserIdQuery;
import com.al.qdt.score.qry.api.queries.FindScoresByWinnerQuery;
import com.al.qdt.score.qry.domain.repositories.ScoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

import static com.al.qdt.common.infrastructure.helpers.Utils.getSortingOrder;
import static com.al.qdt.score.qry.api.exceptions.ScoreNotFoundException.SCORES_BY_USER_ID_AND_WINNER_NOT_FOUND_EXCEPTION_MESSAGE;
import static com.al.qdt.score.qry.api.exceptions.ScoreNotFoundException.SCORES_BY_WINNER_NOT_FOUND_EXCEPTION_MESSAGE;
import static com.al.qdt.score.qry.api.exceptions.ScoreNotFoundException.SCORES_NOT_FOUND_EXCEPTION_MESSAGE;
import static com.al.qdt.score.qry.api.exceptions.ScoreNotFoundException.SCORE_BY_ID_NOT_FOUND_EXCEPTION_MESSAGE;
import static com.al.qdt.score.qry.api.exceptions.ScoreNotFoundException.SCORE_BY_USER_ID_NOT_FOUND_EXCEPTION_MESSAGE;

@Slf4j
@Service
@Transactional(readOnly = true) // for performance optimization, avoids dirty checks on all retrieved entities
@RequiredArgsConstructor
public class ScoreQueryHandler implements QueryHandler {
    private static final long SINGLE_ENTRY = 1L; // single entry

    private final ScoreRepository scoreRepository;

    @Override
    public AbstractMap.SimpleImmutableEntry<Long, List<AbstractEntity>> handle(FindAllScoresQuery query) {
        log.info("Handling find all scores query.");
        // creating Sort instance
        final var sort = getSortingOrder(query.getSortBy(), query.getSortingOrder());
        // creating Pageable instance
        final var pageable = PageRequest.of(query.getCurrentPage(), query.getPageSize(), sort);
        final var page = this.scoreRepository.findAll(pageable);
        if (page.getNumber() == 0 && page.getContent().isEmpty()) {
            throw new ScoreNotFoundException(SCORES_NOT_FOUND_EXCEPTION_MESSAGE);
        }
        return new AbstractMap.SimpleImmutableEntry<Long, List<AbstractEntity>>(page.getTotalElements(), new ArrayList(page.getContent()));
    }

    @Override
    public AbstractMap.SimpleImmutableEntry<Long, List<AbstractEntity>> handle(FindScoreByIdQuery query) {
        final var scoreId = query.getId();
        log.info("Handling find score by id query for id: {}", scoreId.toString());
        final var score = this.scoreRepository.findById(scoreId);
        if (score.isEmpty()) {
            throw new ScoreNotFoundException(String.format(SCORE_BY_ID_NOT_FOUND_EXCEPTION_MESSAGE, scoreId));
        }
        return new AbstractMap.SimpleImmutableEntry<Long, List<AbstractEntity>>(SINGLE_ENTRY, new ArrayList(List.of(score.get())));
    }

    @Override
    public AbstractMap.SimpleImmutableEntry<Long, List<AbstractEntity>> handle(FindScoresByUserIdQuery query) {
        final var userId = query.getUserId();
        log.info("Handling find scores by userId query for userId: {}", userId);
        // creating Sort instance
        final var sort = getSortingOrder(query.getSortBy(), query.getSortingOrder());
        // creating Pageable instance
        final var pageable = PageRequest.of(query.getCurrentPage(), query.getPageSize(), sort);
        final var page = this.scoreRepository.findByUserId(userId, pageable);
        if (page.getNumber() == 0 && page.getContent().isEmpty()) {
            throw new ScoreNotFoundException(String.format(SCORE_BY_USER_ID_NOT_FOUND_EXCEPTION_MESSAGE, userId));
        }
        return new AbstractMap.SimpleImmutableEntry<Long, List<AbstractEntity>>(page.getTotalElements(), new ArrayList(page.getContent()));
    }

    @Override
    public AbstractMap.SimpleImmutableEntry<Long, List<AbstractEntity>> handle(FindScoresByWinnerQuery query) {
        final var winner = query.getWinner();
        log.info("Handling find scores by winner query for winner: {}", winner);
        // creating Sort instance
        final var sort = getSortingOrder(query.getSortBy(), query.getSortingOrder());
        // creating Pageable instance
        final var pageable = PageRequest.of(query.getCurrentPage(), query.getPageSize(), sort);
        final var page = this.scoreRepository.findByWinner(Player.valueOf(winner), pageable);
        if (page.getNumber() == 0 && page.getContent().isEmpty()) {
            throw new ScoreNotFoundException(String.format(SCORES_BY_WINNER_NOT_FOUND_EXCEPTION_MESSAGE, winner));
        }
        return new AbstractMap.SimpleImmutableEntry<Long, List<AbstractEntity>>(page.getTotalElements(), new ArrayList(page.getContent()));
    }

    @Override
    public AbstractMap.SimpleImmutableEntry<Long, List<AbstractEntity>> handle(FindScoresByUserIdAndWinnerQuery query) {
        final var userId = query.getUserId();
        final var winner = query.getWinner();
        log.info("Handling find scores by userId: {} and winner: {}", userId, winner);
        // creating Sort instance
        final var sort = getSortingOrder(query.getSortBy(), query.getSortingOrder());
        // creating Pageable instance
        final var pageable = PageRequest.of(query.getCurrentPage(), query.getPageSize(), sort);
        final var page = this.scoreRepository.findByUserIdAndWinner(userId, Player.valueOf(winner), pageable);
        if (page.getNumber() == 0 && page.getContent().isEmpty()) {
            throw new ScoreNotFoundException(String.format(SCORES_BY_USER_ID_AND_WINNER_NOT_FOUND_EXCEPTION_MESSAGE, userId, winner));
        }
        return new AbstractMap.SimpleImmutableEntry<Long, List<AbstractEntity>>(page.getTotalElements(), new ArrayList(page.getContent()));
    }
}
