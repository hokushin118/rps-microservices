package com.al.qdt.score.cmd.api.commands

import com.al.qdt.score.cmd.base.CommandTests
import com.al.qdt.score.cmd.base.ValidationBaseTest
import spock.lang.Subject
import spock.lang.Title
import spock.lang.Unroll

import javax.validation.ConstraintViolation

import static com.al.qdt.common.infrastructure.helpers.Constants.TEST_UUID
import static com.al.qdt.common.infrastructure.helpers.Constants.USER_ONE_ID
import static com.al.qdt.cqrs.messages.Message.ID_MUST_NOT_BE_NULL
import static com.al.qdt.cqrs.messages.Message.USER_ID_MUST_NOT_BE_NULL

@Title("Testing DeleteScoreCommand class")
class DeleteScoreValidationSpec extends ValidationBaseTest implements CommandTests {

    @Subject
    DeleteScoreCommand expectedDeleteScoreCommand

    // Run before every feature method
    def setup() {
        expectedDeleteScoreCommand = createDeleteScoreCommand TEST_UUID, USER_ONE_ID
    }

    // Run after every feature method
    def cleanup() {
        expectedDeleteScoreCommand = null
    }

    def 'Testing DeleteScoreCommand properties'() {
        expect:
        assert expectedDeleteScoreCommand.id == TEST_UUID: "Id didn't match!"
    }

    def 'Testing DeleteScoreCommand equals() and hashCode() methods'() {
        given: 'Setup test data'
        def actualDeleteScoreCommand = createDeleteScoreCommand TEST_UUID, USER_ONE_ID

        expect:
        assert expectedDeleteScoreCommand == actualDeleteScoreCommand &&
                actualDeleteScoreCommand == expectedDeleteScoreCommand

        and:
        assert expectedDeleteScoreCommand.hashCode() == actualDeleteScoreCommand.hashCode()
    }

    @Unroll
    def 'Testing identifier validating constrains with right parameter - #id and #userId'(UUID id, UUID userId) {
        given: 'Setup test data'
        def deleteScoreCommand = createDeleteScoreCommand id, userId

        when: 'Validate test data'
        def constraintViolations = validator.validate deleteScoreCommand

        then:
        assert constraintViolations.size() == ZERO_VIOLATIONS

        where:
        id        | userId
        TEST_UUID | USER_ONE_ID
    }

    @Unroll
    def 'Testing identifier validating constrains with wrong parameter - #id and #userId, we get error message: #errors'(UUID id, UUID userId, List<String> errors) {
        given: 'Setup test data'
        def deleteScoreCommand = createDeleteScoreCommand id, userId

        when: 'Validate test data'
        Set<ConstraintViolation<DeleteScoreCommand>> constraintViolations = validator.validate deleteScoreCommand

        then:
        assert constraintViolations.size() == count

        def errorList = constraintViolations.stream()
                .filter(x -> errors.stream()
                        .anyMatch(e -> e == ID_MUST_NOT_BE_NULL || e == USER_ID_MUST_NOT_BE_NULL))
                .collect()

        assert errorList.size() == count

        where:
        id        | userId      | count            | errors
        TEST_UUID | null        | SINGLE_VIOLATION | [USER_ID_MUST_NOT_BE_NULL]
        null      | USER_ONE_ID | SINGLE_VIOLATION | [ID_MUST_NOT_BE_NULL]
        null      | null        | DOUBLE_VIOLATION | [ID_MUST_NOT_BE_NULL, USER_ID_MUST_NOT_BE_NULL]
    }
}
