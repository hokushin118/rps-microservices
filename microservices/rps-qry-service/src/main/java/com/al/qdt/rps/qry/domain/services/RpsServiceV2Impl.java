package com.al.qdt.rps.qry.domain.services;

import com.al.qdt.cqrs.infrastructure.QueryDispatcher;
import com.al.qdt.rps.grpc.v1.dto.GameAdminDto;
import com.al.qdt.rps.grpc.v1.dto.GameDto;
import com.al.qdt.rps.grpc.v1.services.ListOfGamesAdminResponse;
import com.al.qdt.rps.grpc.v1.services.ListOfGamesResponse;
import com.al.qdt.rps.qry.api.queries.FindAllGamesQuery;
import com.al.qdt.rps.qry.api.queries.FindGameByIdQuery;
import com.al.qdt.rps.qry.api.queries.FindGamesByUserIdQuery;
import com.al.qdt.rps.qry.domain.entities.Game;
import com.al.qdt.rps.qry.domain.mappers.GameProtoMapper;
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

import static com.al.qdt.rps.qry.infrastructure.config.CacheConfig.GAMES_ADMIN_PROTO_CACHE_NAME;
import static com.al.qdt.rps.qry.infrastructure.config.CacheConfig.GAMES_ADMIN_USER_ID_PROTO_CACHE_NAME;
import static com.al.qdt.rps.qry.infrastructure.config.CacheConfig.GAMES_MY_PROTO_CACHE_NAME;
import static com.al.qdt.rps.qry.infrastructure.config.CacheConfig.GAME_ADMIN_PROTO_CACHE_NAME;
import static com.al.qdt.rps.qry.infrastructure.config.CacheConfig.GAME_CACHE_NAMES;

@Slf4j
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = GAME_CACHE_NAMES)
public class RpsServiceV2Impl implements RpsServiceV2 {
    private final QueryDispatcher queryDispatcher;
    private final GameProtoMapper gameProtoMapper;

    @Override
    @Cacheable(cacheNames = GAMES_ADMIN_PROTO_CACHE_NAME, sync = true)
    public ListOfGamesAdminResponse all() {
        log.info("SERVICE: Getting all games...");
        return this.toListOfGameAdminDto(this.queryDispatcher.send(new FindAllGamesQuery()));
    }

    @Override
    @Cacheable(cacheNames = GAME_ADMIN_PROTO_CACHE_NAME, key = "#id.toString()", sync = true)
    public GameAdminDto findById(UUID id) {
        log.info("SERVICE: Finding game by id: {}.", id.toString());
        final List<Game> games = this.queryDispatcher.send(new FindGameByIdQuery(id));
        return this.gameProtoMapper.toGameAdminDto(games.get(0));
    }

    @Override
    @Cacheable(cacheNames = GAMES_ADMIN_USER_ID_PROTO_CACHE_NAME, key = "#userId.toString()", sync = true)
    public ListOfGamesAdminResponse findByUserId(UUID userId) {
        log.info("SERVICE: Finding games by userId: {}.", userId);
        return this.toListOfGameAdminDto(this.queryDispatcher.send(new FindGamesByUserIdQuery(userId)));
    }

    @Override
    @Cacheable(cacheNames = GAMES_MY_PROTO_CACHE_NAME, key = "#userId.toString()", sync = true)
    public ListOfGamesResponse findMyGames(UUID userId) {
        log.info("SERVICE: Finding scores by userId: {}.", userId);
        return this.toListOfGameDto(this.queryDispatcher.send(new FindGamesByUserIdQuery(userId)));
    }

    /**
     * Empty all games cache after specified time.
     */
    @CacheEvict(value = GAMES_ADMIN_PROTO_CACHE_NAME, allEntries = true)
    @Scheduled(fixedRateString = "#{rpsCacheProperties.getTtlValue()}")
    public void emptyAllCache() {
        log.info("SERVICE: Emptying {} cache...", GAMES_ADMIN_PROTO_CACHE_NAME);
    }

    /**
     * Converts game entities to dto objects.
     *
     * @param games games
     * @return collection of game dto objects
     */
    private ListOfGamesResponse toListOfGameDto(Iterable<Game> games) {
        final List<GameDto> gameDtoList = new ArrayList<>();
        games.forEach(game -> gameDtoList.add(this.gameProtoMapper.toGameDto(game)));
        return ListOfGamesResponse.newBuilder()
                .addAllGames(gameDtoList)
                .build();
    }

    /**
     * Converts game entities to dto objects.
     *
     * @param games games
     * @return collection of game admin dto objects
     */
    private ListOfGamesAdminResponse toListOfGameAdminDto(Iterable<Game> games) {
        final List<GameAdminDto> gameAdminDtoList = new ArrayList<>();
        games.forEach(game -> gameAdminDtoList.add(this.gameProtoMapper.toGameAdminDto(game)));
        return ListOfGamesAdminResponse.newBuilder()
                .addAllGames(gameAdminDtoList)
                .build();
    }
}
