package com.al.qdt.rps.qry.domain.services;

import com.al.qdt.cqrs.infrastructure.QueryDispatcher;
import com.al.qdt.rps.grpc.v1.common.PagingDto;
import com.al.qdt.rps.grpc.v1.common.SortingOrder;
import com.al.qdt.rps.grpc.v1.dto.GameAdminDto;
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

import java.util.UUID;
import java.util.stream.Collectors;

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
    private static final int IDX_COUNT = 1;

    private final QueryDispatcher queryDispatcher;
    private final GameProtoMapper gameProtoMapper;

    @Override
    @Cacheable(cacheNames = GAMES_ADMIN_PROTO_CACHE_NAME, key = "#currentPage.toString().concat('-').concat(#pageSize.toString()).concat('-').concat(#sortBy).concat('-').concat(#sortingOrder.name())", sync = true)
    public ListOfGamesAdminResponse all(int currentPage, int pageSize, String sortBy, SortingOrder sortingOrder) {
        log.info("SERVICE: Getting all games...");
        final var currentPageIndex = currentPage - IDX_COUNT;
        final var findAllGamesQuery = FindAllGamesQuery.builder()
                .currentPage(currentPageIndex)
                .pageSize(pageSize)
                .sortBy(sortBy)
                .sortingOrder(this.gameProtoMapper.toSortingOrder(sortingOrder))
                .build();
        final var games = this.queryDispatcher.send(findAllGamesQuery);
        final var gameAdminDtoList = games.getValue().stream()
                .map(game -> (Game) game)
                .map(this.gameProtoMapper::toGameAdminDto)
                .collect(Collectors.toList());
        return ListOfGamesAdminResponse.newBuilder()
                .addAllGames(gameAdminDtoList)
                .setPaging(PagingDto.newBuilder()
                        .setCurrentPage(currentPage)
                        .setPageSize(pageSize)
                        .setTotalElements((Long) games.getKey())
                        .build())
                .build();
    }

    @Override
    @Cacheable(cacheNames = GAME_ADMIN_PROTO_CACHE_NAME, key = "#id.toString()", sync = true)
    public GameAdminDto findById(UUID id) {
        log.info("SERVICE: Finding game by id: {}.", id.toString());
        final var findGameByIdQuery = FindGameByIdQuery.builder()
                .id(id)
                .build();
        final var games = this.queryDispatcher.send(findGameByIdQuery);
        return games.getValue().stream()
                .map(game -> (Game) game)
                .map(this.gameProtoMapper::toGameAdminDto)
                .findFirst()
                .get();
    }

    @Override
    @Cacheable(cacheNames = GAMES_ADMIN_USER_ID_PROTO_CACHE_NAME, key = "#userId.toString().concat('-').concat(#currentPage.toString()).concat('-').concat(#pageSize.toString()).concat('-').concat(#sortBy).concat('-').concat(#sortingOrder.name())", sync = true)
    public ListOfGamesAdminResponse findByUserId(UUID userId, int currentPage, int pageSize, String sortBy, SortingOrder sortingOrder) {
        log.info("SERVICE: Finding games by userId: {}.", userId);
        final var currentPageIndex = currentPage - IDX_COUNT;
        final var findGamesByUserIdQuery = FindGamesByUserIdQuery.builder()
                .userId(userId)
                .currentPage(currentPageIndex)
                .pageSize(pageSize)
                .sortBy(sortBy)
                .sortingOrder(this.gameProtoMapper.toSortingOrder(sortingOrder))
                .build();
        final var games = this.queryDispatcher.send(findGamesByUserIdQuery);
        final var gameAdminDtoList = games.getValue().stream()
                .map(game -> (Game) game)
                .map(this.gameProtoMapper::toGameAdminDto)
                .collect(Collectors.toList());
        return ListOfGamesAdminResponse.newBuilder()
                .addAllGames(gameAdminDtoList)
                .setPaging(PagingDto.newBuilder()
                        .setCurrentPage(currentPage)
                        .setPageSize(pageSize)
                        .setTotalElements((Long) games.getKey())
                        .build())
                .build();
    }

    @Override
    @Cacheable(cacheNames = GAMES_MY_PROTO_CACHE_NAME, key = "#userId.toString().concat('-').concat(#currentPage.toString()).concat('-').concat(#pageSize.toString()).concat('-').concat(#sortBy).concat('-').concat(#sortingOrder.name())", sync = true)
    public ListOfGamesResponse findMyGames(UUID userId, int currentPage, int pageSize, String sortBy, SortingOrder sortingOrder) {
        log.info("SERVICE: Finding scores by userId: {}.", userId);
        final var currentPageIndex = currentPage - IDX_COUNT;
        final var findGamesByUserIdQuery = FindGamesByUserIdQuery.builder()
                .userId(userId)
                .currentPage(currentPageIndex)
                .pageSize(pageSize)
                .sortBy(sortBy)
                .sortingOrder(this.gameProtoMapper.toSortingOrder(sortingOrder))
                .build();
        final var games = this.queryDispatcher.send(findGamesByUserIdQuery);
        final var gameDtoList = games.getValue().stream()
                .map(game -> (Game) game)
                .map(this.gameProtoMapper::toGameDto)
                .collect(Collectors.toList());
        return ListOfGamesResponse.newBuilder()
                .addAllGames(gameDtoList)
                .setPaging(PagingDto.newBuilder()
                        .setCurrentPage(currentPage)
                        .setPageSize(pageSize)
                        .setTotalElements((Long) games.getKey())
                        .build())
                .build();
    }

    /**
     * Empty all games cache after specified time.
     */
    @CacheEvict(value = GAMES_ADMIN_PROTO_CACHE_NAME, allEntries = true)
    @Scheduled(fixedRateString = "#{rpsCacheProperties.getTtlValue()}")
    public void emptyAllCache() {
        log.info("SERVICE: Emptying {} cache...", GAMES_ADMIN_PROTO_CACHE_NAME);
    }
}
