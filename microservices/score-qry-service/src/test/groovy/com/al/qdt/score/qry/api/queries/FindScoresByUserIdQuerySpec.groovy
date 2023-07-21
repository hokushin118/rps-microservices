package com.al.qdt.score.qry.api.queries

import com.al.qdt.score.qry.base.ValidationBaseTest
import spock.lang.Subject
import spock.lang.Title
import spock.lang.Unroll

import javax.validation.ConstraintViolation

import static com.al.qdt.common.infrastructure.helpers.Constants.USER_ONE_ID
import static com.al.qdt.score.qry.api.queries.FindScoresByUserIdQuery.USER_ID_MUST_NOT_BE_NULL

@Title("Testing FindScoresByUserIdQuery class")
class FindScoresByUserIdQuerySpec extends ValidationBaseTest {

    @Subject
    FindScoresByUserIdQuery expectedFindScoresByUserIdQuery

    // Run before every feature method
    def setup() {
        expectedFindScoresByUserIdQuery = FindScoresByUserIdQuery.builder()
                .userId(USER_ONE_ID)
                .build()
    }

    // Run after every feature method
    def cleanup() {
        expectedFindScoresByUserIdQuery = null
    }

    def 'Testing FindScoresByUserIdQuery properties'() {
        expect:
        assert expectedFindScoresByUserIdQuery.userId == USER_ONE_ID: "User id didn't match!"
    }

    def 'Testing FindScoresByUserIdQuery equals() and hashCode() methods'() {
        given: 'Setup test data'
        def actualFindScoresByUserIdQuery = FindScoresByUserIdQuery.builder()
                .userId(USER_ONE_ID)
                .build()

        expect:
        assert expectedFindScoresByUserIdQuery == actualFindScoresByUserIdQuery &&
                actualFindScoresByUserIdQuery == expectedFindScoresByUserIdQuery

        and:
        assert expectedFindScoresByUserIdQuery.hashCode() == actualFindScoresByUserIdQuery.hashCode()
    }

    @Unroll
    def 'Testing userId validating constrains with right parameters = #userId'(UUID userId) {
        given: 'Setup test data'
        def findScoresByUserIdQuery = FindScoresByUserIdQuery.builder()
                .userId(userId)
                .build()

        when: 'Validate test data'
        def constraintViolations = validator.validate findScoresByUserIdQuery

        then:
        assert constraintViolations.size() == ZERO_VIOLATIONS

        where:
        userId      | _
        USER_ONE_ID | _
    }

    @Unroll
    def 'Testing userId validating constrains with wrong parameter - #userId'(UUID userId) {
        given: 'Setup test data'
        def findScoresByUserIdQuery = FindScoresByUserIdQuery.builder()
                .userId(userId)
                .build()

        when: 'Validate test data'
        Set<ConstraintViolation<FindScoresByUserIdQuery>> constraintViolations = validator.validate findScoresByUserIdQuery

        then:
        assert constraintViolations.size() == SINGLE_VIOLATION
        assert constraintViolations.iterator().next().message == USER_ID_MUST_NOT_BE_NULL

        where:
        userId | _
        null   | _
    }
}
