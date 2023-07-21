package com.al.qdt.rps.qry.domain.services;

import com.al.qdt.common.api.dto.PagingDto;
import com.al.qdt.cqrs.infrastructure.QueryDispatcher;
import com.al.qdt.cqrs.queries.SortingOrder;
import com.al.qdt.rps.qry.api.dto.GameAdminDto;
import com.al.qdt.rps.qry.api.dto.GameAdminPagedResponseDto;
import com.al.qdt.rps.qry.api.dto.GameDto;
import com.al.qdt.rps.qry.api.dto.GamePagedResponseDto;
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

import java.util.UUID;
import java.util.stream.Collectors;

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
    private static final int IDX_COUNT = 1;

    private final QueryDispatcher queryDispatcher;
    private final GameDtoMapper gameDtoMapper;

    @Override
    @Cacheable(cacheNames = GAMES_ADMIN_CACHE_NAME, key = "#currentPage.toString().concat('-').concat(#pageSize.toString()).concat('-').concat(#sortBy).concat('-').concat(#sortingOrder.name())", sync = true)
    public GameAdminPagedResponseDto all(int currentPage, int pageSize, String sortBy, SortingOrder sortingOrder) {
        log.info("SERVICE: Getting all games with pagination...");
        final var currentPageIndex = currentPage - IDX_COUNT;
        final var findAllGamesQuery = FindAllGamesQuery.builder()
                .currentPage(currentPageIndex)
                .pageSize(pageSize)
                .sortBy(sortBy)
                .sortingOrder(sortingOrder)
                .build();
        final var games = this.queryDispatcher.send(findAllGamesQuery);
        final var gameAdminDtoList = games.getValue().stream()
                .map(game -> (Game) game)
                .map(this.gameDtoMapper::toGameAdminDto)
                .collect(Collectors.toList());
        return GameAdminPagedResponseDto.builder()
                .games(gameAdminDtoList)
                .paging(PagingDto.builder()
                        .currentPage(currentPage)
                        .pageSize(pageSize)
                        .totalElements((Long) games.getKey())
                        .build())
                .build();
    }

    @Override
    @Cacheable(cacheNames = GAME_ADMIN_CACHE_NAME, key = "#id.toString()", sync = true)
    public GameAdminDto findById(UUID id) {
        log.info("SERVICE: Finding game by id: {}.", id);
        final var findGameByIdQuery = FindGameByIdQuery.builder()
                .id(id)
                .build();
        final var games = this.queryDispatcher.send(findGameByIdQuery);
        return games.getValue().stream()
                .map(game -> (Game) game)
                .map(this.gameDtoMapper::toGameAdminDto)
                .findFirst()
                .get();
    }

    @Override
    @Cacheable(cacheNames = GAMES_ADMIN_USER_ID_CACHE_NAME, key = "#userId.toString().concat('-').concat(#currentPage.toString()).concat('-').concat(#pageSize.toString()).concat('-').concat(#sortBy).concat('-').concat(#sortingOrder.name())", sync = true)
    public GameAdminPagedResponseDto findByUserId(UUID userId, int currentPage, int pageSize, String sortBy, SortingOrder sortingOrder) {
        log.info("SERVICE: Finding games by userId: {}.", userId);
        final var currentPageIndex = currentPage - IDX_COUNT;
        final var findGamesByUserIdQuery = FindGamesByUserIdQuery.builder()
                .userId(userId)
                .currentPage(currentPageIndex)
                .pageSize(pageSize)
                .sortBy(sortBy)
                .sortingOrder(sortingOrder)
                .build();
        final var games = this.queryDispatcher.send(findGamesByUserIdQuery);
        final var gameAdminDtoList = games.getValue().stream()
                .map(game -> (Game) game)
                .map(this.gameDtoMapper::toGameAdminDto)
                .collect(Collectors.toList());
        return GameAdminPagedResponseDto.builder()
                .games(gameAdminDtoList)
                .paging(PagingDto.builder()
                        .currentPage(currentPage)
                        .pageSize(pageSize)
                        .totalElements((Long) games.getKey())
                        .build())
                .build();
    }

    @Override
    @Cacheable(cacheNames = GAMES_MY_CACHE_NAME, key = "#userId.toString().concat('-').concat(#currentPage.toString()).concat('-').concat(#pageSize.toString()).concat('-').concat(#sortBy).concat('-').concat(#sortingOrder.name())", sync = true)
    public GamePagedResponseDto findMyGames(UUID userId, int currentPage, int pageSize, String sortBy, SortingOrder sortingOrder) {
        log.info("SERVICE: Finding scores by userId: {}.", userId);
        final var currentPageIndex = currentPage - IDX_COUNT;
        final var findGamesByUserIdQuery = FindGamesByUserIdQuery.builder()
                .userId(userId)
                .currentPage(currentPageIndex)
                .pageSize(pageSize)
                .sortBy(sortBy)
                .sortingOrder(sortingOrder)
                .build();
        final var games = this.queryDispatcher.send(findGamesByUserIdQuery);
        final var gameDtoList = games.getValue().stream()
                .map(game -> (Game) game)
                .map(this.gameDtoMapper::toGameDto)
                .collect(Collectors.toList());
        return GamePagedResponseDto.builder()
                .games(gameDtoList)
                .paging(PagingDto.builder()
                        .currentPage(currentPage)
                        .pageSize(pageSize)
                        .totalElements((Long) games.getKey())
                        .build())
                .build();
    }

    /**
     * Empty all games cache after specified time.
     */
    @CacheEvict(value = GAMES_ADMIN_CACHE_NAME, allEntries = true)
    @Scheduled(fixedRateString = "#{rpsCacheProperties.getTtlValue()}")
    public void emptyAllCache() {
        log.info("SERVICE: Emptying {} cache...", GAMES_ADMIN_CACHE_NAME);
    }
}
