package com.al.qdt.score.qry.domain.services;

import com.al.qdt.cqrs.infrastructure.QueryDispatcher;
import com.al.qdt.rps.grpc.v1.common.Player;
import com.al.qdt.rps.grpc.v1.dto.ScoreAdminDto;
import com.al.qdt.rps.grpc.v1.dto.ScoreDto;
import com.al.qdt.rps.grpc.v1.services.ListOfScoresAdminResponse;
import com.al.qdt.rps.grpc.v1.services.ListOfScoresResponse;
import com.al.qdt.score.qry.api.queries.FindAllScoresQuery;
import com.al.qdt.score.qry.api.queries.FindScoreByIdQuery;
import com.al.qdt.score.qry.api.queries.FindScoresByUserIdAndWinnerQuery;
import com.al.qdt.score.qry.api.queries.FindScoresByUserIdQuery;
import com.al.qdt.score.qry.api.queries.FindScoresByWinnerQuery;
import com.al.qdt.score.qry.domain.entities.Score;
import com.al.qdt.score.qry.domain.mappers.ScoreProtoMapper;
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

import static com.al.qdt.score.qry.infrastructure.config.CacheConfig.*;

@Slf4j
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = SCORE_CACHE_NAMES)
public class ScoreServiceV2Impl implements ScoreServiceV2 {
    private static final int IDX = 0;

    private final QueryDispatcher queryDispatcher;
    private final ScoreProtoMapper scoreProtoMapper;

    @Override
    @Cacheable(cacheNames = SCORES_ADMIN_PROTO_CACHE_NAME, sync = true)
    public ListOfScoresAdminResponse all() {
        log.info("SERVICE: Getting all scores.");
        return this.toListOfScoresAdminResponse(this.queryDispatcher.send(new FindAllScoresQuery()));
    }

    @Override
    @Cacheable(cacheNames = SCORE_ADMIN_PROTO_CACHE_NAME, key = "#id.toString()", sync = true)
    public ScoreAdminDto findById(UUID id) {
        log.info("SERVICE: Finding scores by id: {}.", id);
        final List<Score> scores = this.queryDispatcher.send(new FindScoreByIdQuery(id));
        return this.scoreProtoMapper.toScoreAdminDto(scores.get(IDX));
    }

    @Override
    @Cacheable(cacheNames = WINNERS_ADMIN_PROTO_CACHE_NAME, key = "#player.name()", sync = true)
    public ListOfScoresAdminResponse findByWinner(Player player) {
        final var winner = player.name();
        log.info("SERVICE: Finding scores by winner: {}.", winner);
        return this.toListOfScoresAdminResponse(this.queryDispatcher.send(new FindScoresByWinnerQuery(winner)));
    }

    @Override
    @Cacheable(cacheNames = SCORES_ADMIN_USER_ID_PROTO_CACHE_NAME, key = "#userId.toString()", sync = true)
    public ListOfScoresAdminResponse findByUserId(UUID userId) {
        log.info("SERVICE: Finding scores by userId: {}.", userId);
        return this.toListOfScoresAdminResponse(this.queryDispatcher.send(new FindScoresByUserIdQuery(userId)));
    }

    @Cacheable(cacheNames = SCORES_ADMIN_USER_ID_WINNER_PROTO_CACHE_NAME, key = "#userId.toString().concat('-').concat(#player.name())", sync = true)
    public ListOfScoresAdminResponse findByUserIdAndWinner(UUID userId, Player player) {
        final var winner = player.getValueDescriptor().getName();
        log.info("SERVICE: Finding scores by userId: {} and winner: {}.", userId, winner);
        return this.toListOfScoresAdminResponse(this.queryDispatcher.send(new FindScoresByUserIdAndWinnerQuery(userId, winner)));
    }

    @Override
    @Cacheable(cacheNames = SCORES_MY_PROTO_CACHE_NAME, key = "#userId.toString()", sync = true)
    public ListOfScoresResponse findMyScores(UUID userId) {
        log.info("SERVICE: Finding scores by userId: {}.", userId);
        return this.toListOfScoreDto(this.queryDispatcher.send(new FindScoresByUserIdQuery(userId)));
    }

    /**
     * Empty all scores cache after specified time.
     */
    @CacheEvict(value = SCORES_ADMIN_PROTO_CACHE_NAME, allEntries = true)
    @Scheduled(fixedRateString = "#{rpsCacheProperties.getTtlValue()}")
    public void emptyAllCache() {
        log.info("SERVICE: Emptying {} cache...", SCORES_ADMIN_PROTO_CACHE_NAME);
    }

    /**
     * Converts score entities to dto objects.
     *
     * @param scores scores
     * @return collection of score dto objects
     */
    private ListOfScoresResponse toListOfScoreDto(Iterable<Score> scores) {
        final List<ScoreDto> scoreDtoList = new ArrayList<>();
        scores.forEach(score -> scoreDtoList.add(this.scoreProtoMapper.toScoreDto(score)));
        return ListOfScoresResponse.newBuilder()
                .addAllScores(scoreDtoList)
                .build();
    }

    /**
     * Converts score entities to dto objects.
     *
     * @param scores scores
     * @return collection of score admin dto objects
     */
    private ListOfScoresAdminResponse toListOfScoresAdminResponse(Iterable<Score> scores) {
        final List<ScoreAdminDto> scoreAdminDtoList = new ArrayList<>();
        scores.forEach(score -> scoreAdminDtoList.add(this.scoreProtoMapper.toScoreAdminDto(score)));
        return ListOfScoresAdminResponse.newBuilder()
                .addAllScores(scoreAdminDtoList)
                .build();
    }
}
