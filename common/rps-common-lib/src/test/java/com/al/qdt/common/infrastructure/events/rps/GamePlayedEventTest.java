package com.al.qdt.common.infrastructure.events.rps;

import com.al.qdt.common.domain.base.EventTests;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.JSONException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import static com.al.qdt.common.domain.enums.Hand.ROCK;
import static com.al.qdt.common.infrastructure.helpers.Constants.TEST_UUID;
import static com.al.qdt.common.infrastructure.helpers.Constants.USER_ONE_ID;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Testing GamePlayedEvent class")
class GamePlayedEventTest implements EventTests {
    private static final String EXPECTED_JSON = "{\"id\":\"748873ec-f887-4090-93ff-f8b8cbb34c7a\",\"user_id\":\"558873ec-f887-4090-93ff-f8b8cbb34c7a\",\"hand\":\"ROCK\"}";

    GamePlayedEvent expectedGamePlayedEvent;

    @BeforeEach
    void setUp() {
        this.expectedGamePlayedEvent = createGamePlayedEvent();
    }

    @AfterEach
    void tearDown() {
        this.expectedGamePlayedEvent = null;
    }

    @Test
    @DisplayName("Testing GamePlayedEvent properties")
    void GamePlayedEventPropertiesTest() {
        assertAll("Testing GamePlayedEvent",
                () -> assertAll("BaseEvent properties",
                        () -> assertEquals(TEST_UUID, this.expectedGamePlayedEvent.getId(), "Id didn't match!")
                ),
                () -> assertAll("GamePlayedEvent properties",
                        () -> assertEquals(USER_ONE_ID, this.expectedGamePlayedEvent.getUserId(), "User id didn't match!"),
                        () -> assertEquals(ROCK, this.expectedGamePlayedEvent.getHand(), "Hand didn't match!")
                )
        );
    }

    @Test
    @DisplayName("Testing GamePlayedEvent serialization")
    void gamePlayedEventSerializationTest() throws JsonProcessingException, JSONException {
        final var actualJson = this.objectMapper.writeValueAsString(this.expectedGamePlayedEvent);

        assertNotNull(actualJson);
        assertThat(actualJson, containsString("id"));
        assertThat(actualJson, containsString("user_id"));
        assertThat(actualJson, containsString("hand"));
        assertThat(actualJson, not(containsString("game")));

        JSONAssert.assertEquals(EXPECTED_JSON, actualJson, true);
    }

    @Test
    @DisplayName("Testing GamePlayedEvent deserialization")
    void gamePlayedEventDeserializationTest() throws JsonProcessingException {
        final var actualGamePlayedEvent = this.objectMapper.readValue(EXPECTED_JSON, GamePlayedEvent.class);

        assertNotNull(actualGamePlayedEvent);
        assertEquals(TEST_UUID, actualGamePlayedEvent.getId());
        assertEquals(USER_ONE_ID, actualGamePlayedEvent.getUserId());
        assertEquals(ROCK, actualGamePlayedEvent.getHand());
    }

    @Test
    @DisplayName("Testing GamePlayedEvent equals() and hashCode() methods")
    void equalsAndHashCodeTest() {
        final var actualGamePlayedEvent = createGamePlayedEvent();

        assertTrue(this.expectedGamePlayedEvent.equals(actualGamePlayedEvent) &&
                actualGamePlayedEvent.equals(this.expectedGamePlayedEvent));
        assertEquals(this.expectedGamePlayedEvent.hashCode(), actualGamePlayedEvent.hashCode());
    }
}
