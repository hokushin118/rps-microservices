package com.al.qdt.score.qry.domain.services;

import com.al.qdt.common.api.dto.PagingDto;
import com.al.qdt.cqrs.queries.SortingOrder;
import com.al.qdt.score.qry.api.dto.ScoreAdminDto;
import com.al.qdt.score.qry.api.dto.ScoreAdminPagedResponseDto;
import com.al.qdt.common.domain.enums.Player;
import com.al.qdt.cqrs.infrastructure.QueryDispatcher;
import com.al.qdt.score.qry.api.dto.ScorePagedResponseDto;
import com.al.qdt.score.qry.api.queries.FindAllScoresQuery;
import com.al.qdt.score.qry.api.queries.FindScoreByIdQuery;
import com.al.qdt.score.qry.api.queries.FindScoresByUserIdAndWinnerQuery;
import com.al.qdt.score.qry.api.queries.FindScoresByUserIdQuery;
import com.al.qdt.score.qry.api.queries.FindScoresByWinnerQuery;
import com.al.qdt.score.qry.domain.entities.Score;
import com.al.qdt.score.qry.domain.mappers.ScoreDtoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.Collectors;

import static com.al.qdt.score.qry.infrastructure.config.CacheConfig.SCORES_ADMIN_CACHE_NAME;
import static com.al.qdt.score.qry.infrastructure.config.CacheConfig.SCORES_ADMIN_USER_ID_CACHE_NAME;
import static com.al.qdt.score.qry.infrastructure.config.CacheConfig.SCORES_MY_CACHE_NAME;
import static com.al.qdt.score.qry.infrastructure.config.CacheConfig.SCORES_ADMIN_USER_ID_WINNER_CACHE_NAME;
import static com.al.qdt.score.qry.infrastructure.config.CacheConfig.SCORE_ADMIN_CACHE_NAME;
import static com.al.qdt.score.qry.infrastructure.config.CacheConfig.SCORE_CACHE_NAMES;
import static com.al.qdt.score.qry.infrastructure.config.CacheConfig.WINNERS_ADMIN_CACHE_NAME;

@Slf4j
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = SCORE_CACHE_NAMES)
public class ScoreServiceV1Impl implements ScoreServiceV1 {
    private static final int IDX_COUNT = 1;

    private final QueryDispatcher queryDispatcher;
    private final ScoreDtoMapper scoreDtoMapper;

    @Override
    @Cacheable(cacheNames = SCORES_ADMIN_CACHE_NAME, key = "#currentPage.toString().concat('-').concat(#pageSize.toString()).concat('-').concat(#sortBy).concat('-').concat(#sortingOrder.name())", sync = true)
    public ScoreAdminPagedResponseDto all(int currentPage, int pageSize, String sortBy, SortingOrder sortingOrder) {
        log.info("SERVICE: Getting all scores.");
        final var currentPageIndex = currentPage - IDX_COUNT;
        final var findAllScoresQuery = FindAllScoresQuery.builder()
                .currentPage(currentPageIndex)
                .pageSize(pageSize)
                .sortBy(sortBy)
                .sortingOrder(sortingOrder)
                .build();
        final var scores = this.queryDispatcher.send(findAllScoresQuery);
        final var scoreAdminDtoList = scores.getValue().stream()
                .map(score -> (Score) score)
                .map(this.scoreDtoMapper::toScoreAdminDto)
                .collect(Collectors.toList());
        return ScoreAdminPagedResponseDto.builder()
                .scores(scoreAdminDtoList)
                .pagination(PagingDto.builder()
                        .currentPage(currentPage)
                        .pageSize(pageSize)
                        .totalElements((Long) scores.getKey())
                        .build())
                .build();
    }

    @Override
    @Cacheable(cacheNames = SCORE_ADMIN_CACHE_NAME, key = "#id.toString()", sync = true)
    public ScoreAdminDto findById(UUID id) {
        log.info("SERVICE: Finding scores by id: {}.", id);
        final var findScoreByIdQuery = FindScoreByIdQuery.builder()
                .id(id)
                .build();
        final var scores = this.queryDispatcher.send(findScoreByIdQuery);
        return scores.getValue().stream()
                .map(score -> (Score) score)
                .map(this.scoreDtoMapper::toScoreAdminDto)
                .findFirst()
                .get();
    }

    @Override
    @Cacheable(cacheNames = SCORES_ADMIN_USER_ID_CACHE_NAME, key = "#userId.toString().concat('-').concat(#currentPage.toString()).concat('-').concat(#pageSize.toString()).concat('-').concat(#sortBy).concat('-').concat(#sortingOrder.name())", sync = true)
    public ScoreAdminPagedResponseDto findByUserId(UUID userId, int currentPage, int pageSize, String sortBy, SortingOrder sortingOrder) {
        log.info("SERVICE: Finding scores by userId: {}.", userId);
        final var currentPageIndex = currentPage - IDX_COUNT;
        final var findScoresByUserIdQuery = FindScoresByUserIdQuery.builder()
                .userId(userId)
                .currentPage(currentPageIndex)
                .pageSize(pageSize)
                .sortBy(sortBy)
                .sortingOrder(sortingOrder)
                .build();
        final var scores = this.queryDispatcher.send(findScoresByUserIdQuery);
        final var scoreAdminDtoList = scores.getValue().stream()
                .map(score -> (Score) score)
                .map(this.scoreDtoMapper::toScoreAdminDto)
                .collect(Collectors.toList());
        return ScoreAdminPagedResponseDto.builder()
                .scores(scoreAdminDtoList)
                .pagination(PagingDto.builder()
                        .currentPage(currentPage)
                        .pageSize(pageSize)
                        .totalElements((Long) scores.getKey())
                        .build())
                .build();
    }

    @Override
    @Cacheable(cacheNames = WINNERS_ADMIN_CACHE_NAME, key = "#player.name().concat('-').concat(#currentPage.toString()).concat('-').concat(#pageSize.toString()).concat('-').concat(#sortBy).concat('-').concat(#sortingOrder.name())", sync = true)
    public ScoreAdminPagedResponseDto findByWinner(Player player, int currentPage, int pageSize, String sortBy, SortingOrder sortingOrder) {
        final var winner = player.name();
        log.info("SERVICE: Finding scores by winner: {}.", winner);
        final var currentPageIndex = currentPage - IDX_COUNT;
        final var findScoresByWinnerQuery = FindScoresByWinnerQuery.builder()
                .winner(winner)
                .currentPage(currentPageIndex)
                .pageSize(pageSize)
                .sortBy(sortBy)
                .sortingOrder(sortingOrder)
                .build();
        final var scores = this.queryDispatcher.send(findScoresByWinnerQuery);
        final var scoreAdminDtoList = scores.getValue().stream()
                .map(score -> (Score) score)
                .map(this.scoreDtoMapper::toScoreAdminDto)
                .collect(Collectors.toList());
        return ScoreAdminPagedResponseDto.builder()
                .scores(scoreAdminDtoList)
                .pagination(PagingDto.builder()
                        .currentPage(currentPage)
                        .pageSize(pageSize)
                        .totalElements((Long) scores.getKey())
                        .build())
                .build();
    }

    @Override
    @Cacheable(cacheNames = SCORES_ADMIN_USER_ID_WINNER_CACHE_NAME, key = "#userId.toString().concat('-').concat(#player.name()).concat('-').concat(#currentPage.toString()).concat('-').concat(#pageSize.toString()).concat('-').concat(#sortBy).concat('-').concat(#sortingOrder.name())", sync = true)
    public ScoreAdminPagedResponseDto findByUserIdAndWinner(UUID userId, Player player, int currentPage, int pageSize, String sortBy, SortingOrder sortingOrder) {
        final var winner = player.name();
        log.info("SERVICE: Finding scores by userId: {} and winner: {}.", userId, winner);
        final var currentPageIndex = currentPage - IDX_COUNT;
        final var findScoresByUserIdAndWinnerQuery = FindScoresByUserIdAndWinnerQuery.builder()
                .userId(userId)
                .winner(winner)
                .currentPage(currentPageIndex)
                .pageSize(pageSize)
                .sortBy(sortBy)
                .sortingOrder(sortingOrder)
                .build();
        final var scores = this.queryDispatcher.send(findScoresByUserIdAndWinnerQuery);
        final var scoreAdminDtoList = scores.getValue().stream()
                .map(score -> (Score) score)
                .map(this.scoreDtoMapper::toScoreAdminDto)
                .collect(Collectors.toList());
        return ScoreAdminPagedResponseDto.builder()
                .scores(scoreAdminDtoList)
                .pagination(PagingDto.builder()
                        .currentPage(currentPage)
                        .pageSize(pageSize)
                        .totalElements((Long) scores.getKey())
                        .build())
                .build();
    }

    @Override
    @Cacheable(cacheNames = SCORES_MY_CACHE_NAME, key = "#userId.toString().concat('-').concat(#currentPage.toString()).concat('-').concat(#pageSize.toString()).concat('-').concat(#sortBy).concat('-').concat(#sortingOrder.name())", sync = true)
    public ScorePagedResponseDto findMyScores(UUID userId, int currentPage, int pageSize, String sortBy, SortingOrder sortingOrder) {
        log.info("SERVICE: Finding scores by userId: {}.", userId);
        final var currentPageIndex = currentPage - IDX_COUNT;
        final var findScoresByUserIdQuery = FindScoresByUserIdQuery.builder()
                .userId(userId)
                .currentPage(currentPageIndex)
                .pageSize(pageSize)
                .sortBy(sortBy)
                .sortingOrder(sortingOrder)
                .build();
        final var scores = this.queryDispatcher.send(findScoresByUserIdQuery);
        final var scoreDtoList = scores.getValue().stream()
                .map(score -> (Score) score)
                .map(this.scoreDtoMapper::toScoreDto)
                .collect(Collectors.toList());
        return ScorePagedResponseDto.builder()
                .scores(scoreDtoList)
                .pagination(PagingDto.builder()
                        .currentPage(currentPage)
                        .pageSize(pageSize)
                        .totalElements((Long) scores.getKey())
                        .build())
                .build();
    }

    /**
     * Empty all scores cache after specified time.
     */
    @CacheEvict(value = SCORES_ADMIN_CACHE_NAME, allEntries = true)
    @Scheduled(fixedRateString = "#{rpsCacheProperties.getTtlValue()}")
    public void emptyAllCache() {
        log.info("SERVICE: Emptying {} cache...", SCORES_ADMIN_CACHE_NAME);
    }
}
