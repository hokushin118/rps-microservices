package com.al.qdt.rps.qry.domain.services;

import com.al.qdt.common.api.dto.GameAdminDto;
import com.al.qdt.common.api.dto.GameDto;
import com.al.qdt.cqrs.infrastructure.QueryDispatcher;
import com.al.qdt.rps.qry.api.queries.FindAllGamesQuery;
import com.al.qdt.rps.qry.api.queries.FindGameByIdQuery;
import com.al.qdt.rps.qry.api.queries.FindGamesByUserIdQuery;
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

import static com.al.qdt.rps.qry.infrastructure.config.CacheConfig.GAMES_ADMIN_CACHE_NAME;
import static com.al.qdt.rps.qry.infrastructure.config.CacheConfig.GAMES_ADMIN_USER_ID_CACHE_NAME;
import static com.al.qdt.rps.qry.infrastructure.config.CacheConfig.GAMES_MY_CACHE_NAME;
import static com.al.qdt.rps.qry.infrastructure.config.CacheConfig.GAME_ADMIN_CACHE_NAME;
import static com.al.qdt.rps.qry.infrastructure.config.CacheConfig.GAME_CACHE_NAMES;

@Slf4j
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = GAME_CACHE_NAMES)
public class RpsServiceV1Impl implements RpsServiceV1 {
    private final QueryDispatcher queryDispatcher;
    private final GameDtoMapper gameDtoMapper;

    @Override
    @Cacheable(cacheNames = GAMES_ADMIN_CACHE_NAME, sync = true)
    public Iterable<GameAdminDto> all() {
        log.info("SERVICE: Getting all games...");
        return this.toListOfGameAdminDto(this.queryDispatcher.send(new FindAllGamesQuery()));
    }

    @Override
    @Cacheable(cacheNames = GAME_ADMIN_CACHE_NAME, key = "#id.toString()", sync = true)
    public GameAdminDto findById(UUID id) {
        log.info("SERVICE: Finding game by id: {}.", id);
        final List<Game> games = this.queryDispatcher.send(new FindGameByIdQuery(id));
        return this.gameDtoMapper.toGameAdminDto(games.get(0));
    }

    @Override
    @Cacheable(cacheNames = GAMES_ADMIN_USER_ID_CACHE_NAME, key = "#userId.toString()", sync = true)
    public Iterable<GameAdminDto> findByUserId(UUID userId) {
        log.info("SERVICE: Finding games by userId: {}.", userId);
        return this.toListOfGameAdminDto(this.queryDispatcher.send(new FindGamesByUserIdQuery(userId)));
    }

    @Override
    @Cacheable(cacheNames = GAMES_MY_CACHE_NAME, key = "#userId.toString()", sync = true)
    public Iterable<GameDto> findMyGames(UUID userId) {
        log.info("SERVICE: Finding scores by userId: {}.", userId);
        return this.toListOfGameDto(this.queryDispatcher.send(new FindGamesByUserIdQuery(userId)));
    }

    /**
     * Empty all games cache after specified time.
     */
    @CacheEvict(value = GAMES_ADMIN_CACHE_NAME, allEntries = true)
    @Scheduled(fixedRateString = "#{rpsCacheProperties.getTtlValue()}")
    public void emptyAllCache() {
        log.info("SERVICE: Emptying {} cache...", GAMES_ADMIN_CACHE_NAME);
    }

    /**
     * Converts game entities to dto objects.
     *
     * @param games games
     * @return collection of game dto objects
     */
    private Iterable<GameDto> toListOfGameDto(Iterable<Game> games) {
        final List<GameDto> gameDtoList = new ArrayList<>();
        games.forEach(game -> gameDtoList.add(this.gameDtoMapper.toGameDto(game)));
        return gameDtoList;
    }

    /**
     * Converts game entities to dto objects.
     *
     * @param games games
     * @return collection of game admin dto objects
     */
    private Iterable<GameAdminDto> toListOfGameAdminDto(Iterable<Game> games) {
        final List<GameAdminDto> gameAdminDtoList = new ArrayList<>();
        games.forEach(game -> gameAdminDtoList.add(this.gameDtoMapper.toGameAdminDto(game)));
        return gameAdminDtoList;
    }
}
