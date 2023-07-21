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
import static com.al.qdt.common.infrastructure.helpers.Constants.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Testing GameDto class")
class GameDtoTest implements DtoTests {
    GameDto expectedGameDto;

    @BeforeEach
    void setUp() {
        this.expectedGameDto = createGameDto(USER_ONE_ID);
    }

    @AfterEach
    void tearDown() {
        this.expectedGameDto = null;
    }

    @Test
    @DisplayName("Testing GameDto properties")
    void gameDtoPropertiesTest() {
        assertAll("Testing GameDto",
                () -> assertEquals(TEST_ID, this.expectedGameDto.getId(), "Id didn't match!"),
                () -> assertEquals(ROCK.name(), this.expectedGameDto.getHand(), "Hand didn't match!")
        );
    }

    @Test
    @DisplayName("Testing GameDto serialization")
    void gameDtoSerializationTest() throws JsonProcessingException, JSONException {
        final var actualJson = objectMapper.writeValueAsString(this.expectedGameDto);

        assertNotNull(actualJson);
        assertThat(actualJson, containsString("id"));
        assertThat(actualJson, containsString("hand"));
        assertThat(actualJson, not(containsString("game")));

        JSONAssert.assertEquals(GAME_EXPECTED_JSON, actualJson, true);
    }

    @Test
    @DisplayName("Testing GameDto deserialization")
    void gameDtoDeserializationTest() throws JsonProcessingException {
        final var actualGameDto = objectMapper.readValue(GAME_EXPECTED_JSON, GameDto.class);

        assertNotNull(actualGameDto);
        assertEquals(TEST_ID, actualGameDto.getId());
        assertEquals(this.expectedGameDto.getId(), actualGameDto.getId());
        assertEquals(ROCK.name(), actualGameDto.getHand());
        assertEquals(this.expectedGameDto.getHand(), actualGameDto.getHand());
    }

    @Test
    @DisplayName("Testing GameDto equals() and hashCode() methods")
    void equalsAndHashCodeTest() {
        final var actualGameDto = createGameDto(USER_ONE_ID);

        assertTrue(this.expectedGameDto.equals(actualGameDto) &&
                actualGameDto.equals(this.expectedGameDto));
        assertEquals(this.expectedGameDto.hashCode(), actualGameDto.hashCode());
    }
}
