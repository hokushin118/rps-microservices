package com.al.qdt.rps.qry.infrastructure.handlers;

import com.al.qdt.cqrs.domain.AbstractEntity;
import com.al.qdt.rps.qry.api.exceptions.GameNotFoundException;
import com.al.qdt.rps.qry.api.queries.FindAllGamesQuery;
import com.al.qdt.rps.qry.api.queries.FindGameByIdQuery;
import com.al.qdt.rps.qry.api.queries.FindGamesByUserIdQuery;
import com.al.qdt.rps.qry.domain.repositories.GameRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.al.qdt.common.infrastructure.helpers.Utils.getSortingOrder;
import static com.al.qdt.rps.qry.api.exceptions.GameNotFoundException.GAMES_NOT_FOUND_EXCEPTION_MESSAGE;
import static com.al.qdt.rps.qry.api.exceptions.GameNotFoundException.GAME_BY_ID_NOT_FOUND_EXCEPTION_MESSAGE;
import static com.al.qdt.rps.qry.api.exceptions.GameNotFoundException.GAME_BY_USER_ID_NOT_FOUND_EXCEPTION_MESSAGE;

@Slf4j
@Service
@Transactional(readOnly = true) // for performance optimization, avoids dirty checks on all retrieved entities
@RequiredArgsConstructor
public class RpsQueryHandler implements QueryHandler {
    private static final long SINGLE_ENTRY = 1L; // single entry

    private final GameRepository gameRepository;

    @Override
    public AbstractMap.SimpleImmutableEntry<Long, List<AbstractEntity>> handle(FindAllGamesQuery query) {
        log.info("Handling find all games query.");
        // creating Sort instance
        final var sort = getSortingOrder(query.getSortBy(), query.getSortingOrder());
        // creating Pageable instance
        final var pageable = PageRequest.of(query.getCurrentPage(), query.getPageSize(), sort);
        final var page = this.gameRepository.findAll(pageable);
        if (page.getNumber() == 0 && page.getContent().isEmpty()) {
            throw new GameNotFoundException(GAMES_NOT_FOUND_EXCEPTION_MESSAGE);
        }
        return new AbstractMap.SimpleImmutableEntry<Long, List<AbstractEntity>>(page.getTotalElements(), new ArrayList(page.getContent()));
    }

    @Override
    public AbstractMap.SimpleImmutableEntry<Long, List<AbstractEntity>> handle(FindGameByIdQuery query) {
        final var gameId = query.getId();
        log.info("Handling find game by id query for id: {}.", gameId.toString());
        final var game = this.gameRepository.findById(gameId);
        if (game.isEmpty()) {
            throw new GameNotFoundException(String.format(GAME_BY_ID_NOT_FOUND_EXCEPTION_MESSAGE, gameId));
        }
        return new AbstractMap.SimpleImmutableEntry<Long, List<AbstractEntity>>(SINGLE_ENTRY, new ArrayList(List.of(game.get())));
    }

    @Override
    public AbstractMap.SimpleImmutableEntry<Long, List<AbstractEntity>> handle(FindGamesByUserIdQuery query) {
        final var userId = query.getUserId();
        log.info("Handling find games by userId query for userId: {}.", userId);
        // creating Sort instance
        final var sort = getSortingOrder(query.getSortBy(), query.getSortingOrder());
        // creating Pageable instance
        final var pageable = PageRequest.of(query.getCurrentPage(), query.getPageSize(), sort);
        final var page = this.gameRepository.findByUserId(userId, pageable);
        if (page.getNumber() == 0 && page.getContent().isEmpty()) {
            throw new GameNotFoundException(String.format(GAME_BY_USER_ID_NOT_FOUND_EXCEPTION_MESSAGE, userId));
        }
        return new AbstractMap.SimpleImmutableEntry<Long, List<AbstractEntity>>(page.getTotalElements(), new ArrayList(page.getContent()));
    }
}
