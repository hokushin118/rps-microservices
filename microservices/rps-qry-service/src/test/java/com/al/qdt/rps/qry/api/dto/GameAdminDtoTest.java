package com.al.qdt.rps.qry.api.dto;

import com.al.qdt.rps.qry.base.DtoTests;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.JSONException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import static com.al.qdt.common.domain.enums.Hand.ROCK;
import static com.al.qdt.common.infrastructure.helpers.Constants.GAME_ADMIN_EXPECTED_JSON;
import static com.al.qdt.common.infrastructure.helpers.Constants.TEST_ID;
import static com.al.qdt.common.infrastructure.helpers.Constants.USER_ONE_ID;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Testing GameAdminDto class")
class GameAdminDtoTest implements DtoTests {
    GameAdminDto expectedGameAdminDto;

    @BeforeEach
    void setUp() {
        this.expectedGameAdminDto = createGameAdminDto(USER_ONE_ID);
    }

    @AfterEach
    void tearDown() {
        this.expectedGameAdminDto = null;
    }

    @Test
    @DisplayName("Testing GameAdminDto properties")
    void GameAdminDtoPropertiesTest() {
        assertAll("Testing GameAdminDto",
                () -> assertEquals(TEST_ID, this.expectedGameAdminDto.getId(), "Id didn't match!"),
                () -> assertEquals(USER_ONE_ID.toString(), this.expectedGameAdminDto.getUserId(), "User id didn't match!"),
                () -> assertEquals(ROCK.name(), this.expectedGameAdminDto.getHand(), "Hand didn't match!")
        );
    }

    @Test
    @DisplayName("Testing GameAdminDto serialization")
    void GameAdminDtoSerializationTest() throws JsonProcessingException, JSONException {
        final var actualJson = objectMapper.writeValueAsString(this.expectedGameAdminDto);

        assertNotNull(actualJson);
        assertThat(actualJson, containsString("id"));
        assertThat(actualJson, containsString("user_id"));
        assertThat(actualJson, containsString("hand"));
        assertThat(actualJson, not(containsString("game")));

        JSONAssert.assertEquals(GAME_ADMIN_EXPECTED_JSON, actualJson, true);
    }

    @Test
    @DisplayName("Testing GameAdminDto deserialization")
    void GameAdminDtoDeserializationTest() throws JsonProcessingException {
        final var actualGameAdminDto = objectMapper.readValue(GAME_ADMIN_EXPECTED_JSON, GameAdminDto.class);

        assertNotNull(actualGameAdminDto);
        assertEquals(TEST_ID, actualGameAdminDto.getId());
        assertEquals(this.expectedGameAdminDto.getId(), actualGameAdminDto.getId());
        assertEquals(USER_ONE_ID.toString(), actualGameAdminDto.getUserId());
        assertEquals(this.expectedGameAdminDto.getUserId(), actualGameAdminDto.getUserId());
        assertEquals(ROCK.name(), actualGameAdminDto.getHand());
        assertEquals(this.expectedGameAdminDto.getHand(), actualGameAdminDto.getHand());
    }

    @Test
    @DisplayName("Testing GameAdminDto equals() and hashCode() methods")
    void equalsAndHashCodeTest() {
        final var actualGameAdminDto = createGameAdminDto(USER_ONE_ID);

        assertTrue(this.expectedGameAdminDto.equals(actualGameAdminDto) &&
                actualGameAdminDto.equals(this.expectedGameAdminDto));
        assertEquals(this.expectedGameAdminDto.hashCode(), actualGameAdminDto.hashCode());
    }
}
