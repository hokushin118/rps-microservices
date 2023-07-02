package com.al.qdt.score.qry.infrastructure.handlers;

import com.al.qdt.common.infrastructure.events.rps.GameDeletedEvent;
import com.al.qdt.common.infrastructure.events.score.ScoresAddedEvent;
import com.al.qdt.common.infrastructure.events.score.ScoresDeletedEvent;
import com.al.qdt.score.qry.domain.repositories.ScoreRepository;
import com.al.qdt.score.qry.domain.mappers.ScoreMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;

import static com.al.qdt.score.qry.infrastructure.config.CacheConfig.SCORES_ADMIN_CACHE_NAME;
import static com.al.qdt.score.qry.infrastructure.config.CacheConfig.SCORES_ADMIN_PROTO_CACHE_NAME;
import static com.al.qdt.score.qry.infrastructure.config.CacheConfig.SCORES_ADMIN_USER_ID_CACHE_NAME;
import static com.al.qdt.score.qry.infrastructure.config.CacheConfig.SCORES_ADMIN_USER_ID_PROTO_CACHE_NAME;
import static com.al.qdt.score.qry.infrastructure.config.CacheConfig.SCORES_ADMIN_USER_ID_WINNER_CACHE_NAME;
import static com.al.qdt.score.qry.infrastructure.config.CacheConfig.SCORES_ADMIN_USER_ID_WINNER_PROTO_CACHE_NAME;
import static com.al.qdt.score.qry.infrastructure.config.CacheConfig.SCORES_MY_CACHE_NAME;
import static com.al.qdt.score.qry.infrastructure.config.CacheConfig.SCORES_MY_PROTO_CACHE_NAME;
import static com.al.qdt.score.qry.infrastructure.config.CacheConfig.SCORE_ADMIN_CACHE_NAME;
import static com.al.qdt.score.qry.infrastructure.config.CacheConfig.SCORE_ADMIN_PROTO_CACHE_NAME;
import static com.al.qdt.score.qry.infrastructure.config.CacheConfig.SCORE_CACHE_NAMES;
import static com.al.qdt.score.qry.infrastructure.config.CacheConfig.WINNERS_ADMIN_CACHE_NAME;
import static com.al.qdt.score.qry.infrastructure.config.CacheConfig.WINNERS_ADMIN_PROTO_CACHE_NAME;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
@CacheConfig(cacheNames = SCORE_CACHE_NAMES)
public class ScoreEventHandler implements EventHandler {
    private final ScoreRepository scoreRepository;
    private final ScoreMapper scoreMapper;

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = SCORES_ADMIN_CACHE_NAME, allEntries = true),
            @CacheEvict(cacheNames = SCORES_ADMIN_PROTO_CACHE_NAME, allEntries = true),
            @CacheEvict(cacheNames = WINNERS_ADMIN_CACHE_NAME, allEntries = true),
            @CacheEvict(cacheNames = WINNERS_ADMIN_PROTO_CACHE_NAME, allEntries = true)})
    public void on(@Valid ScoresAddedEvent event) {
        final var scoreId = event.getId();
        log.info("Handling score added event with id: {}", scoreId);
        if (!this.scoreRepository.existsById(scoreId)) {
            this.scoreRepository.save(this.scoreMapper.toEntity(event));
        }
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = SCORE_ADMIN_CACHE_NAME, key = "#event.id.toString()"),
            @CacheEvict(cacheNames = SCORE_ADMIN_PROTO_CACHE_NAME, key = "#event.id.toString()"),
            @CacheEvict(cacheNames = SCORES_ADMIN_USER_ID_CACHE_NAME, key = "#event.userId.toString()"),
            @CacheEvict(cacheNames = SCORES_ADMIN_USER_ID_PROTO_CACHE_NAME, key = "#event.userId.toString()"),
            @CacheEvict(cacheNames = SCORES_MY_CACHE_NAME, key = "#event.userId.toString()"),
            @CacheEvict(cacheNames = SCORES_MY_PROTO_CACHE_NAME, key = "#event.userId.toString()"),
            @CacheEvict(cacheNames = SCORES_ADMIN_USER_ID_WINNER_CACHE_NAME, key = "'regex:#event.userId.toString()'+'.*'"),
            @CacheEvict(cacheNames = SCORES_ADMIN_USER_ID_WINNER_PROTO_CACHE_NAME, key = "'regex:#event.userId.toString()'+'.*'"),
            @CacheEvict(cacheNames = SCORES_ADMIN_CACHE_NAME, allEntries = true),
            @CacheEvict(cacheNames = SCORES_ADMIN_PROTO_CACHE_NAME, allEntries = true),
            @CacheEvict(cacheNames = WINNERS_ADMIN_CACHE_NAME, allEntries = true),
            @CacheEvict(cacheNames = WINNERS_ADMIN_PROTO_CACHE_NAME, allEntries = true)})
    public void on(@Valid ScoresDeletedEvent event) {
        final var scoreId = event.getId();
        log.info("Handling score deleted event with id: {}", scoreId);
        this.scoreRepository.deleteById(scoreId);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = SCORE_ADMIN_CACHE_NAME, key = "#event.id.toString()"),
            @CacheEvict(cacheNames = SCORE_ADMIN_PROTO_CACHE_NAME, key = "#event.id.toString()"),
            @CacheEvict(cacheNames = SCORES_ADMIN_USER_ID_CACHE_NAME, key = "#event.userId.toString()"),
            @CacheEvict(cacheNames = SCORES_ADMIN_USER_ID_PROTO_CACHE_NAME, key = "#event.userId.toString()"),
            @CacheEvict(cacheNames = SCORES_MY_CACHE_NAME, key = "#event.userId.toString()"),
            @CacheEvict(cacheNames = SCORES_MY_PROTO_CACHE_NAME, key = "#event.userId.toString()"),
            @CacheEvict(cacheNames = SCORES_ADMIN_USER_ID_WINNER_CACHE_NAME, key = "'regex:#event.userId.toString()'+'.*'"),
            @CacheEvict(cacheNames = SCORES_ADMIN_USER_ID_WINNER_PROTO_CACHE_NAME, key = "'regex:#event.userId.toString()'+'.*'"),
            @CacheEvict(cacheNames = SCORES_ADMIN_CACHE_NAME, allEntries = true),
            @CacheEvict(cacheNames = SCORES_ADMIN_PROTO_CACHE_NAME, allEntries = true),
            @CacheEvict(cacheNames = WINNERS_ADMIN_CACHE_NAME, allEntries = true),
            @CacheEvict(cacheNames = WINNERS_ADMIN_PROTO_CACHE_NAME, allEntries = true)})
    public void on(@Valid GameDeletedEvent event) {
        final var scoreId = event.getId();
        log.info("Handling game deleted event with id: {}", scoreId);
        this.scoreRepository.deleteById(scoreId);
    }
}
