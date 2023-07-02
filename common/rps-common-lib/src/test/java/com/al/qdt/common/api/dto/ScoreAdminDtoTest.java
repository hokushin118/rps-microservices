package com.al.qdt.common.api.dto;

import com.al.qdt.common.domain.base.DtoTests;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.JSONException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import static com.al.qdt.common.domain.enums.Player.USER;
import static com.al.qdt.common.infrastructure.helpers.Constants.SCORE_ADMIN_EXPECTED_JSON;
import static com.al.qdt.common.infrastructure.helpers.Constants.TEST_ID;
import static com.al.qdt.common.infrastructure.helpers.Constants.USER_ONE_ID;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Testing ScoreAdminDto class")
class ScoreAdminDtoTest implements DtoTests {
    ScoreAdminDto expectedScoreAdminDto;

    @BeforeEach
    void setUp() {
        this.expectedScoreAdminDto = createScoreAdminDto();
    }

    @AfterEach
    void tearDown() {
        this.expectedScoreAdminDto = null;
    }

    @Test
    @DisplayName("Testing ScoreAdminDto properties")
    void scoreAdminDtoPropertiesTest() {
        assertAll("Testing ScoreAdminDto",
                () -> assertEquals(TEST_ID, this.expectedScoreAdminDto.getId(), "Id didn't match!"),
                () -> assertEquals(USER_ONE_ID.toString(), this.expectedScoreAdminDto.getUserId(), "UserId didn't match!"),
                () -> assertEquals(USER.name(), this.expectedScoreAdminDto.getWinner(), "Winner didn't match!")
        );
    }

    @Test
    @DisplayName("Testing ScoreAdminDto serialization")
    void scoreAdminDtoSerializationTest() throws JsonProcessingException, JSONException {
        final var actualJson = objectMapper.writeValueAsString(this.expectedScoreAdminDto);

        assertNotNull(actualJson);
        assertThat(actualJson, containsString("id"));
        assertThat(actualJson, containsString("user_id"));
        assertThat(actualJson, containsString("winner"));
        assertThat(actualJson, not(containsString("score")));

        JSONAssert.assertEquals(SCORE_ADMIN_EXPECTED_JSON, actualJson, true);
    }

    @Test
    @DisplayName("Testing ScoreAdminDto deserialization")
    void scoreAdminDtoDeserializationTest() throws JsonProcessingException {
        final var actualScoreAdminDto = objectMapper.readValue(SCORE_ADMIN_EXPECTED_JSON, ScoreAdminDto.class);

        assertNotNull(actualScoreAdminDto);
        assertEquals(TEST_ID, actualScoreAdminDto.getId());
        assertEquals(this.expectedScoreAdminDto.getId(), actualScoreAdminDto.getId());
        assertEquals(USER_ONE_ID.toString(), actualScoreAdminDto.getUserId());
        assertEquals(this.expectedScoreAdminDto.getUserId(), actualScoreAdminDto.getUserId());
        assertEquals(USER.name(), actualScoreAdminDto.getWinner());
        assertEquals(this.expectedScoreAdminDto.getWinner(), actualScoreAdminDto.getWinner());
    }

    @Test
    @DisplayName("Testing ScoreAdminDto equals() and hashCode() methods")
    void equalsAndHashCodeTest() {
        final var actualScoreAdminDto = createScoreAdminDto();

        assertTrue(this.expectedScoreAdminDto.equals(actualScoreAdminDto) &&
                actualScoreAdminDto.equals(this.expectedScoreAdminDto));
        assertEquals(this.expectedScoreAdminDto.hashCode(), actualScoreAdminDto.hashCode());
    }
}
