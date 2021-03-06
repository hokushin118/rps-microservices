package com.al.qdt.score.qry.services;

import com.al.qdt.common.dto.ScoreDto;
import com.al.qdt.common.enums.Player;
import com.al.qdt.cqrs.infrastructure.QueryDispatcher;
import com.al.qdt.score.qry.domain.Score;
import com.al.qdt.score.qry.queries.FindAllScoresQuery;
import com.al.qdt.score.qry.queries.FindScoreByIdQuery;
import com.al.qdt.score.qry.queries.FindScoresByWinnerQuery;
import com.al.qdt.score.qry.services.mappers.ScoreDtoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "scoresCache")
public class ScoreServiceV1Impl implements ScoreServiceV1 {
    private final QueryDispatcher queryDispatcher;
    private final ScoreDtoMapper scoreDtoMapper;

    @Override
    @Cacheable(cacheNames = "scores", sync = true)
    public Iterable<ScoreDto> all() {
        log.info("SERVICE: Getting all scores.");
        return this.toListOfScoreDto(this.queryDispatcher.send(new FindAllScoresQuery()));
    }

    @Override
    @Cacheable(cacheNames = "score", key = "#id.toString()", sync = true)
    public ScoreDto findById(UUID id) {
        log.info("SERVICE: Finding scores by id: {}.", id.toString());
        final List<Score> scores = this.queryDispatcher.send(new FindScoreByIdQuery(id));
        return this.scoreDtoMapper.toDto(scores.get(0));
    }

    @Override
    @Cacheable(cacheNames = "winners", key = "#winner.name()", sync = true)
    public Iterable<ScoreDto> findByWinner(Player winner) {
        log.info("SERVICE: Finding scores by winner: {}.", winner);
        return this.toListOfScoreDto(this.queryDispatcher.send(new FindScoresByWinnerQuery(winner.name())));
    }

    /**
     * Converts score entities to dto objects.
     *
     * @param scores scores
     * @return collection of score dto objects
     */
    private Iterable<ScoreDto> toListOfScoreDto(Iterable<Score> scores) {
        final List<ScoreDto> scoreDtoList = new ArrayList<>();
        scores.forEach(score -> scoreDtoList.add(this.scoreDtoMapper.toDto(score)));
        return scoreDtoList;
    }
}
