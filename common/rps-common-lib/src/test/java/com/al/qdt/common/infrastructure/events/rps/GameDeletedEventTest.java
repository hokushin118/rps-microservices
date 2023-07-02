package com.al.qdt.common.infrastructure.events.rps;

import com.al.qdt.common.domain.base.EventTests;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.JSONException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import static com.al.qdt.common.infrastructure.helpers.Constants.TEST_UUID;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Testing GameDeletedEvent class")
@Tag(value = "event")
class GameDeletedEventTest implements EventTests {
    private static final String EXPECTED_JSON = "{\"id\":\"748873ec-f887-4090-93ff-f8b8cbb34c7a\", \"user_id\":\"558873ec-f887-4090-93ff-f8b8cbb34c7a\"}";

    GameDeletedEvent expectedGameDeletedEvent;

    @BeforeEach
    void setUp() {
        this.expectedGameDeletedEvent = createGameDeletedEvent();
    }

    @AfterEach
    void tearDown() {
        this.expectedGameDeletedEvent = null;
    }

    @Test
    @DisplayName("Testing GameDeletedEvent properties")
    void GameDeletedEventPropertiesTest() {
        assertAll("Testing GameDeletedEvent",
                () -> assertAll("BaseEvent properties",
                        () -> assertEquals(TEST_UUID, this.expectedGameDeletedEvent.getId(), "Id didn't match!")
                )
        );
    }

    @Test
    @DisplayName("Testing GameDeletedEvent serialization")
    void gameDeletedEventSerializationTest() throws JsonProcessingException, JSONException {
        final var actualJson = this.objectMapper.writeValueAsString(this.expectedGameDeletedEvent);

        assertNotNull(actualJson);
        assertThat(actualJson, containsString("id"));
        assertThat(actualJson, containsString("user_id"));
        assertThat(actualJson, not(containsString("game")));

        JSONAssert.assertEquals(EXPECTED_JSON, actualJson, true);
    }

    @Test
    @DisplayName("Testing GameDeletedEvent deserialization")
    void gameDeletedEventDeserializationTest() throws JsonProcessingException {
        final var actualGameDeletedEvent = this.objectMapper.readValue(EXPECTED_JSON, GameDeletedEvent.class);

        assertNotNull(actualGameDeletedEvent);
        assertEquals(TEST_UUID, actualGameDeletedEvent.getId());
    }

    @Test
    @DisplayName("Testing GameDeletedEvent equals() and hashCode() methods")
    void equalsAndHashCodeTest() {
        final var actualGameDeletedEvent = createGameDeletedEvent();

        assertTrue(this.expectedGameDeletedEvent.equals(actualGameDeletedEvent) &&
                actualGameDeletedEvent.equals(this.expectedGameDeletedEvent));
        assertEquals(this.expectedGameDeletedEvent.hashCode(), actualGameDeletedEvent.hashCode());
    }
}
