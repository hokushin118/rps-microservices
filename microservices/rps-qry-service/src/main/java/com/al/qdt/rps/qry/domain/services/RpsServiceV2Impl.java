package com.al.qdt.rps.qry.domain.services;

import com.al.qdt.cqrs.infrastructure.QueryDispatcher;
import com.al.qdt.rps.grpc.v1.dto.GameDto;
import com.al.qdt.rps.grpc.v1.services.ListOfGamesResponse;
import com.al.qdt.rps.qry.api.queries.FindAllGamesQuery;
import com.al.qdt.rps.qry.api.queries.FindGameByIdQuery;
import com.al.qdt.rps.qry.api.queries.FindGamesByUsernameQuery;
import com.al.qdt.rps.qry.domain.entities.Game;
import com.al.qdt.rps.qry.domain.mappers.GameProtoMapper;
import com.google.protobuf.StringValue;
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
public class RpsServiceV2Impl implements RpsServiceV2 {
    private final QueryDispatcher queryDispatcher;
    private final GameProtoMapper gameProtoMapper;

    @Override
    @Cacheable(cacheNames = GAMES_PROTO_CACHE_NAME, sync = true)
    public ListOfGamesResponse all() {
        log.info("SERVICE: Getting all games...");
        return this.toListOfGameDto(this.queryDispatcher.send(new FindAllGamesQuery()));
    }

    /**
     * Empty all games cache after specified time.
     */
    @CacheEvict(value = GAMES_PROTO_CACHE_NAME, allEntries = true)
    @Scheduled(fixedRateString = "#{rpsCacheProperties.getTtlValue()}")
    public void emptyAllCache() {
        log.info("SERVICE: Emptying {} cache...", GAMES_PROTO_CACHE_NAME);
    }

    @Override
    @Cacheable(cacheNames = GAME_PROTO_CACHE_NAME, key = "#id.toString()", sync = true)
    public GameDto findById(UUID id) {
        log.info("SERVICE: Finding game by id: {}.", id.toString());
        final List<Game> games = this.queryDispatcher.send(new FindGameByIdQuery(id));
        return this.gameProtoMapper.toDto(games.get(0));
    }

    @Override
    @Cacheable(cacheNames = USERNAME_PROTO_CACHE_NAME, key = "#username", sync = true)
    public ListOfGamesResponse findByUsername(StringValue username) {
        final var gameUsername = username.getValue();
        log.info("SERVICE: Finding game by username: {}.", gameUsername);
        return this.toListOfGameDto(this.queryDispatcher.send(new FindGamesByUsernameQuery(gameUsername)));
    }

    /**
     * Converts game entities to dto objects.
     *
     * @param games games
     * @return collection of game dto objects
     */
    private ListOfGamesResponse toListOfGameDto(Iterable<Game> games) {
        final List<GameDto> gameDtoList = new ArrayList<>();
        games.forEach(game -> gameDtoList.add(this.gameProtoMapper.toDto(game)));
        return ListOfGamesResponse.newBuilder()
                .addAllGames(gameDtoList)
                .build();
    }
}
