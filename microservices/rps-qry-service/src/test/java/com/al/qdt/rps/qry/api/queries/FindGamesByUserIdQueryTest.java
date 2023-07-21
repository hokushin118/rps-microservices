package com.al.qdt.rps.qry.api.queries;

import com.al.qdt.rps.qry.base.ValidationBaseTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

import java.util.UUID;

import static com.al.qdt.common.infrastructure.helpers.Constants.USER_ONE_ID;
import static com.al.qdt.rps.qry.domain.entities.Game.USER_ID_MUST_NOT_BE_NULL;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Testing FindGamesByUserIdQuery class")
@Tag(value = "query")
class FindGamesByUserIdQueryTest extends ValidationBaseTest {
    FindGamesByUserIdQuery expectedFindGamesByUserIdQuery;

    @BeforeEach
    void setUp() {
        this.expectedFindGamesByUserIdQuery = FindGamesByUserIdQuery.builder()
                .userId(USER_ONE_ID)
                .build();
    }

    @AfterEach
    void tearDown() {
        this.expectedFindGamesByUserIdQuery = null;
    }

    @Test
    @DisplayName("Testing FindGamesByUserIdQuery properties")
    void findGamesByUserIdQueryPropertiesTest() {
        assertAll("FindGamesByUserIdQuery properties",
                () -> assertEquals(USER_ONE_ID, this.expectedFindGamesByUserIdQuery.getUserId(), "User id didn't match!")
        );
    }

    @Test
    @DisplayName("Testing FindGamesByUserIdQuery equals() and hashCode() methods")
    void equalsAndHashCodeTest() {
        final var actualFindGamesByUserIdQuery = FindGamesByUserIdQuery.builder()
                .userId(USER_ONE_ID)
                .build();

        assertTrue(this.expectedFindGamesByUserIdQuery.equals(actualFindGamesByUserIdQuery) &&
                actualFindGamesByUserIdQuery.equals(this.expectedFindGamesByUserIdQuery));
        assertEquals(this.expectedFindGamesByUserIdQuery.hashCode(), actualFindGamesByUserIdQuery.hashCode());
    }

    @Test
    @DisplayName("Testing userId validating constrains with right parameters")
    void userIdIsValid() {
        final var constraintViolations = validator.validate(this.expectedFindGamesByUserIdQuery);

        assertEquals(ZERO_VIOLATIONS, constraintViolations.size());
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("Testing userId validating constrains with wrong parameters")
    void userIdIsBlank(UUID userId) {
        final var actualFindGamesByUserIdQuery = FindGamesByUserIdQuery.builder()
                .userId(userId)
                .build();
        final var constraintViolations = validator.validate(actualFindGamesByUserIdQuery);

        assertEquals(SINGLE_VIOLATION, constraintViolations.size());
        assertEquals(USER_ID_MUST_NOT_BE_NULL, constraintViolations.iterator().next().getMessage());
    }
}
