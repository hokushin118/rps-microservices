package com.al.qdt.score.qry.domain.entities

import com.al.qdt.common.domain.enums.Player
import com.al.qdt.score.qry.base.EntityTests
import com.al.qdt.score.qry.base.ValidationBaseTest
import spock.lang.Subject
import spock.lang.Title
import spock.lang.Unroll

import javax.validation.ConstraintViolation

import static com.al.qdt.common.domain.enums.Player.USER
import static com.al.qdt.common.domain.enums.Player.MACHINE
import static com.al.qdt.common.domain.enums.Player.DRAW
import static com.al.qdt.common.infrastructure.helpers.Constants.TEST_UUID
import static com.al.qdt.common.infrastructure.helpers.Constants.USER_ONE_ID
import static com.al.qdt.score.qry.domain.entities.Score.WINNER_MUST_NOT_BE_NULL

@Title("Testing Score entity class")
class ScoreSpec extends ValidationBaseTest implements EntityTests {

    @Subject
    Score expectedScore

    // Run before every feature method
    def setup() {
        expectedScore = createScore TEST_UUID, USER_ONE_ID, USER
    }

    // Run after every feature method
    def cleanup() {
        expectedScore = null
    }

    def 'Testing Score properties'() {
        expect:
        assert expectedScore.id == TEST_UUID: "Id didn't match!"
        assert expectedScore.winner == USER: "Player didn't match!"
    }

    def 'Testing equals() and hash() methods of identical objects'() {
        given: 'Setup test data'
        def actualScore = expectedScore

        expect:
        assert expectedScore == actualScore && actualScore == expectedScore

        and:
        assert expectedScore.hashCode() == actualScore.hashCode()
    }

    def 'Testing equals() and hash() methods with identical UUIDs'() {
        given: 'Setup test data'
        def actualScore = createScore TEST_UUID, USER_ONE_ID, USER

        expect:
        assert expectedScore == actualScore && actualScore == expectedScore

        and:
        assert expectedScore.hashCode() == actualScore.hashCode()
    }

    def 'Testing equals() and hash() methods with objects with different UUIDs'() {
        given: 'Setup test data'
        def actualScore = createSecondScore()

        expect:
        assert expectedScore != actualScore && actualScore != expectedScore

        and:
        assert expectedScore.hashCode() != actualScore.hashCode()
    }

    def 'Testing equals() and hash() methods with objects with nullable UUIDs'() {
        given: 'Setup test data'
        def expectedScore = createScoreWithNulUUID()

        and:
        def actualScore = createScoreWithNulUUID()

        expect:
        assert expectedScore != actualScore && actualScore != expectedScore

        and:
        assert expectedScore.hashCode() == actualScore.hashCode()
    }

    @Unroll
    def 'Testing winner validating constrains with right parameters = #winner'(Player winner) {
        given: 'Setup test data'
        def score = createScore TEST_UUID, USER_ONE_ID, winner

        when: 'Validate test data'
        def constraintViolations = validator.validate score

        then:
        assert constraintViolations.size() == ZERO_VIOLATIONS

        where:
        winner  | _
        USER    | _
        MACHINE | _
        DRAW    | _
    }

    @Unroll
    def 'Testing winner validating constrains with wrong parameter - #winner'(Player winner) {
        given: 'Setup test data'
        def score = createScore TEST_UUID, USER_ONE_ID, winner

        when: 'Validate test data'
        Set<ConstraintViolation<Score>> constraintViolations = validator.validate score

        then:
        assert constraintViolations.size() == SINGLE_VIOLATION
        assert constraintViolations.iterator().next().message == WINNER_MUST_NOT_BE_NULL

        where:
        winner | _
        null   | _
    }
}
