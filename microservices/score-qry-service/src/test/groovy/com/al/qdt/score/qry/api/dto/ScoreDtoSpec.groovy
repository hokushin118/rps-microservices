package com.al.qdt.score.qry.api.dto

import com.al.qdt.score.qry.base.DtoTests
import org.skyscreamer.jsonassert.JSONAssert
import spock.lang.Subject
import spock.lang.Title

import static com.al.qdt.common.domain.enums.Player.USER
import static com.al.qdt.common.infrastructure.helpers.Constants.SCORE_EXPECTED_JSON
import static com.al.qdt.common.infrastructure.helpers.Constants.TEST_ID

@Title("Testing ScoreDto class")
class ScoreDtoSpec extends DtoTests {

    @Subject
    ScoreDto expectedScoreDto

    // Run before every feature method
    def setup() {
        this.expectedScoreDto = createScoreDto()
    }

    // Run after every feature method
    def cleanup() {
        this.expectedScoreDto = null
    }

    def 'Testing ScoreDto properties'() {
        expect:
        assert this.expectedScoreDto.id == TEST_ID: "Id didn't match!"
        assert this.expectedScoreDto.winner == USER.name(): "Winner didn't match!"
    }

    def 'Testing ScoreDto serialization'() {
        given: 'Setup test data'
        final var actualJson = objectMapper.writeValueAsString this.expectedScoreDto

        expect:
        assert actualJson
        assert actualJson.contains('id')
        assert actualJson.contains('winner')
        assert !actualJson.contains('score')

        JSONAssert.assertEquals SCORE_EXPECTED_JSON, actualJson, true
    }

    def 'Testing ScoreDto deserialization'() {
        given: 'Setup test data'
        final var actualScoreDto = objectMapper.readValue SCORE_EXPECTED_JSON, ScoreDto.class

        expect:
        assert actualScoreDto
        assert actualScoreDto.id == TEST_ID
        assert actualScoreDto.id == this.expectedScoreDto.id
        assert actualScoreDto.winner == USER.name()
        assert actualScoreDto.winner == this.expectedScoreDto.winner
    }

    def 'Testing ScoreDto equals() and hashCode() methods'() {
        given: 'Setup test data'
        final var actualScoreDto = createScoreDto()

        expect:
        assert this.expectedScoreDto == actualScoreDto &&
                actualScoreDto == this.expectedScoreDto

        and:
        assert actualScoreDto.hashCode() == this.expectedScoreDto.hashCode()
    }
}
