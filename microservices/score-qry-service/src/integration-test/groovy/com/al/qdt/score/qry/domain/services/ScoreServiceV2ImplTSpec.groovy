package com.al.qdt.score.qry.domain.services

import com.al.qdt.rps.grpc.v1.dto.ScoreDto
import com.al.qdt.score.qry.base.AbstractIntegrationTests
import com.al.qdt.score.qry.base.ProtoTests
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Subject
import spock.lang.Title

import static com.al.qdt.rps.grpc.v1.common.Player.USER
import static com.al.qdt.common.infrastructure.helpers.Constants.TEST_UUID
import static com.al.qdt.score.qry.infrastructure.config.CacheConfig.SCORES_ADMIN_PROTO_CACHE_NAME
import static com.al.qdt.score.qry.infrastructure.config.CacheConfig.SCORE_ADMIN_PROTO_CACHE_NAME
import static com.al.qdt.score.qry.infrastructure.config.CacheConfig.WINNERS_ADMIN_PROTO_CACHE_NAME

@Title("Integration testing of the ScoreServiceV2Impl class")
class ScoreServiceV2ImplTSpec extends AbstractIntegrationTests implements ProtoTests {

    @Subject
    @Autowired
    ScoreServiceV2Impl scoreService

    def 'Testing injections'() {
        expect:
        assert scoreService
    }

    def 'Testing caching functionality of the findById() method, scores should be cached'() {
        when: 'Calling the api with right parameter'
        scoreService.findById TEST_UUID

        then: 'No exception thrown'
        noExceptionThrown()

        and: 'Data exists in the cache'
        def actualScore = getCachedScoreDtoById(TEST_UUID, ScoreDto.class, SCORE_ADMIN_PROTO_CACHE_NAME).get() as ScoreDto
        assert expectedScore.winner.name() == actualScore.winner

        and:
        "Im-memory cache contains $SCORE_ADMIN_PROTO_CACHE_NAME cache"
        assert cacheManager.cacheNames.contains(SCORE_ADMIN_PROTO_CACHE_NAME)
    }

    def 'Testing caching functionality of the all() method, scores should be cached'() {
        when: 'Calling the api'
        def actualScores = scoreService.all()

        then: 'No exception thrown'
        noExceptionThrown()

        and: 'List of data returns from server'
        assert actualScores.scoresList.size() == EXPECTED_COLLECTION_SIZE

        and: 'Im-memory cache size is not empty'
        assert !isEmpty(cacheManager.getCache(SCORES_ADMIN_PROTO_CACHE_NAME))
    }

    def 'Testing caching functionality of the findByWinner() method, scores should be cached'() {
        when: 'Calling the api with right parameter'
        def actualScores = scoreService.findByWinner USER

        then: 'No exception thrown'
        noExceptionThrown()

        and: 'List of data returns from server'
        assert actualScores.scoresList.size() == EXPECTED_COLLECTION_SIZE

        and: 'Im-memory cache size is not empty'
        assert !isEmpty(cacheManager.getCache(WINNERS_ADMIN_PROTO_CACHE_NAME))
    }
}
