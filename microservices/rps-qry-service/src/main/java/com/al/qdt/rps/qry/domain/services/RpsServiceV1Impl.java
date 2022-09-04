package com.al.qdt.rps.qry.domain.services;

import com.al.qdt.common.api.dto.GameDto;
import com.al.qdt.cqrs.infrastructure.QueryDispatcher;
import com.al.qdt.rps.qry.api.queries.FindAllGamesQuery;
import com.al.qdt.rps.qry.api.queries.FindGameByIdQuery;
import com.al.qdt.rps.qry.api.queries.FindGamesByUsernameQuery;
import com.al.qdt.rps.qry.domain.entities.Game;
import com.al.qdt.rps.qry.domain.mappers.GameDtoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.al.qdt.rps.qry.infrastructure.config.CacheConfig.*;

@Slf4j
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = GAME_CACHE_NAMES)
public class RpsServiceV1Impl implements RpsServiceV1 {
    private final QueryDispatcher queryDispatcher;
    private final GameDtoMapper gameDtoMapper;

    @Override
    @Cacheable(cacheNames = GAMES_CACHE_NAME, sync = true)
    public Iterable<GameDto> all() {
        log.info("SERVICE: Getting all games...");
        return this.toListOfGameDto(this.queryDispatcher.send(new FindAllGamesQuery()));
    }

    /**
     * Empty all games cache after specified time.
     */
    @CacheEvict(value = GAMES_CACHE_NAME, allEntries = true)
    @Scheduled(fixedRateString = "#{rpsCacheProperties.getTtlValue()}")
    public void emptyAllCache() {
        log.info("SERVICE: Emptying {} cache...", GAMES_CACHE_NAME);
    }

    @Override
    @Cacheable(cacheNames = GAME_CACHE_NAME, key = "#id.toString()", sync = true)
    public GameDto findById(UUID id) {
        log.info("SERVICE: Finding game by id: {}.", id.toString());
        final List<Game> games = this.queryDispatcher.send(new FindGameByIdQuery(id));
        return this.gameDtoMapper.toDto(games.get(0));
    }

    @Override
    @Cacheable(cacheNames = USERNAME_CACHE_NAME, key = "#username", sync = true)
    public Iterable<GameDto> findByUsername(String username) {
        log.info("SERVICE: Finding game by username: {}.", username);
        return this.toListOfGameDto(this.queryDispatcher.send(new FindGamesByUsernameQuery(username)));
    }

    /**
     * Converts game entities to dto objects.
     *
     * @param games games
     * @return collection of game dto objects
     */
    private Iterable<GameDto> toListOfGameDto(Iterable<Game> games) {
        final List<GameDto> gameDtoList = new ArrayList<>();
        games.forEach(game -> gameDtoList.add(this.gameDtoMapper.toDto(game)));
        return gameDtoList;
    }
}
