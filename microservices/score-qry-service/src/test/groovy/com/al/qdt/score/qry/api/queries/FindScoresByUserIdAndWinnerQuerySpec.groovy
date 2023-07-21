package com.al.qdt.score.qry.api.queries

import com.al.qdt.score.qry.base.ValidationBaseTest
import spock.lang.Subject
import spock.lang.Title
import spock.lang.Unroll

import javax.validation.ConstraintViolation

import static com.al.qdt.common.infrastructure.helpers.Constants.TEST_WINNER
import static com.al.qdt.common.infrastructure.helpers.Constants.USER_ONE_ID
import static com.al.qdt.score.qry.api.queries.FindScoresByUserIdAndWinnerQuery.USER_ID_MUST_NOT_BE_NULL

@Title("Testing FindScoresByUserIdAndWinnerQuery class")
class FindScoresByUserIdAndWinnerQuerySpec extends ValidationBaseTest {

    @Subject
    FindScoresByUserIdAndWinnerQuery expectedFindScoresByUserIdAndWinnerQuery

    // Run before every feature method
    def setup() {
        expectedFindScoresByUserIdAndWinnerQuery = FindScoresByUserIdAndWinnerQuery.builder()
                .userId(USER_ONE_ID)
                .winner(TEST_WINNER)
                .build()
    }

    // Run after every feature method
    def cleanup() {
        expectedFindScoresByUserIdAndWinnerQuery = null
    }

    def 'Testing FindScoresByUserIdAndWinnerQuery properties'() {
        expect:
        assert expectedFindScoresByUserIdAndWinnerQuery.userId == USER_ONE_ID: "User id didn't match!"
    }

    def 'Testing FindScoresByUserIdAndWinnerQuery equals() and hashCode() methods'() {
        given: 'Setup test data'
        def actualFindScoresByUserIdAndWinnerQuery = FindScoresByUserIdAndWinnerQuery.builder()
                .userId(USER_ONE_ID)
                .winner(TEST_WINNER)
                .build()

        expect:
        assert expectedFindScoresByUserIdAndWinnerQuery == actualFindScoresByUserIdAndWinnerQuery &&
                actualFindScoresByUserIdAndWinnerQuery == expectedFindScoresByUserIdAndWinnerQuery

        and:
        assert expectedFindScoresByUserIdAndWinnerQuery.hashCode() == actualFindScoresByUserIdAndWinnerQuery.hashCode()
    }

    @Unroll
    def 'Testing userId validating constrains with right parameters = #userId'(UUID userId) {
        given: 'Setup test data'
        def findScoresByUserIdAndWinnerQuery = FindScoresByUserIdAndWinnerQuery.builder()
                .userId(userId)
                .winner(TEST_WINNER)
                .build()

        when: 'Validate test data'
        def constraintViolations = validator.validate findScoresByUserIdAndWinnerQuery

        then:
        assert constraintViolations.size() == ZERO_VIOLATIONS

        where:
        userId      | _
        USER_ONE_ID | _
    }

    @Unroll
    def 'Testing userId validating constrains with wrong parameter - #userId'(UUID userId) {
        given: 'Setup test data'
        def findScoresByUserIdAndWinnerQuery = FindScoresByUserIdAndWinnerQuery.builder()
                .userId(userId)
                .winner(TEST_WINNER)
                .build()

        when: 'Validate test data'
        Set<ConstraintViolation<FindScoresByUserIdAndWinnerQuery>> constraintViolations = validator.validate findScoresByUserIdAndWinnerQuery

        then:
        assert constraintViolations.size() == SINGLE_VIOLATION
        assert constraintViolations.iterator().next().message == USER_ID_MUST_NOT_BE_NULL

        where:
        userId | _
        null   | _
    }
}
