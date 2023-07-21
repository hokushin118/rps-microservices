package com.al.qdt.score.qry.api.dto

import com.al.qdt.score.qry.base.DtoTests
import org.skyscreamer.jsonassert.JSONAssert
import spock.lang.Subject
import spock.lang.Title

import static com.al.qdt.common.domain.enums.Player.USER
import static com.al.qdt.common.infrastructure.helpers.Constants.SCORE_ADMIN_EXPECTED_JSON
import static com.al.qdt.common.infrastructure.helpers.Constants.TEST_ID
import static com.al.qdt.common.infrastructure.helpers.Constants.USER_ONE_ID

@Title("Testing ScoreAdminDto class")
class ScoreAdminDtoSpec extends DtoTests {

    @Subject
    ScoreAdminDto expectedScoreAdminDto

    // Run before every feature method
    def setup() {
        this.expectedScoreAdminDto = createScoreAdminDto()
    }

    // Run after every feature method
    def cleanup() {
        this.expectedScoreAdminDto = null
    }

    def 'Testing ScoreAdminDto properties'() {
        expect:
        assert this.expectedScoreAdminDto.id == TEST_ID: "Id didn't match!"
        assert this.expectedScoreAdminDto.userId == USER_ONE_ID.toString(): "UserId didn't match!"
        assert this.expectedScoreAdminDto.winner == USER.name(): "Winner didn't match!"
    }

    def 'Testing ScoreAdminDto serialization'() {
        given: 'Setup test data'
        final var actualJson = objectMapper.writeValueAsString this.expectedScoreAdminDto

        expect:
        assert actualJson
        assert actualJson.contains('id')
        assert actualJson.contains('user_id')
        assert actualJson.contains('winner')
        assert !actualJson.contains('score')

        JSONAssert.assertEquals SCORE_ADMIN_EXPECTED_JSON, actualJson, true
    }

    def 'Testing ScoreAdminDto deserialization'() {
        given: 'Setup test data'
        final var actualScoreAdminDto = objectMapper.readValue SCORE_ADMIN_EXPECTED_JSON, ScoreAdminDto.class

        expect:
        assert actualScoreAdminDto
        assert actualScoreAdminDto.id == TEST_ID
        assert actualScoreAdminDto.id == this.expectedScoreAdminDto.id
        assert actualScoreAdminDto.userId == USER_ONE_ID.toString()
        assert actualScoreAdminDto.userId == this.expectedScoreAdminDto.userId
        assert actualScoreAdminDto.winner == USER.name()
        assert actualScoreAdminDto.winner == this.expectedScoreAdminDto.winner
    }

    def 'Testing ScoreAdminDto equals() and hashCode() methods'() {
        given: 'Setup test data'
        final var actualScoreAdminDto = createScoreAdminDto()

        expect:
        assert this.expectedScoreAdminDto == actualScoreAdminDto &&
                actualScoreAdminDto == this.expectedScoreAdminDto

        and:
        assert actualScoreAdminDto.hashCode() == this.expectedScoreAdminDto.hashCode()
    }
}
