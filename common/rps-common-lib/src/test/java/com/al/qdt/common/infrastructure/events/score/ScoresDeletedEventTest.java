package com.al.qdt.common.infrastructure.events.score;

import com.al.qdt.common.domain.base.EventTests;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.JSONException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

@DisplayName("Testing ScoresDeletedEvent class")
class ScoresDeletedEventTest implements EventTests {
    private static final String EXPECTED_JSON = "{\"id\":\"748873ec-f887-4090-93ff-f8b8cbb34c7a\",\"user_id\":\"558873ec-f887-4090-93ff-f8b8cbb34c7a\"}";

    ScoresDeletedEvent expectedScoresDeletedEvent;

    @BeforeEach
    void setUp() {
        this.expectedScoresDeletedEvent = createScoresDeletedEvent();
    }

    @AfterEach
    void tearDown() {
        this.expectedScoresDeletedEvent = null;
    }

    @Test
    @DisplayName("Testing ScoresDeletedEvent properties")
    void ScoresDeletedEventPropertiesTest() {
        assertAll("Testing ScoresDeletedEvent",
                () -> assertAll("BaseEvent properties",
                        () -> assertEquals(TEST_UUID, this.expectedScoresDeletedEvent.getId(), "Id didn't match!")
                )
        );
    }

    @Test
    @DisplayName("Testing ScoresDeletedEvent serialization")
    void scoresDeletedEventSerializationTest() throws JsonProcessingException, JSONException {
        final var actualJson = this.objectMapper.writeValueAsString(this.expectedScoresDeletedEvent);

        assertNotNull(actualJson);
        assertThat(actualJson, containsString("id"));
        assertThat(actualJson, containsString("user_id"));
        assertThat(actualJson, not(containsString("score")));

        JSONAssert.assertEquals(EXPECTED_JSON, actualJson, true);
    }

    @Test
    @DisplayName("Testing ScoresDeletedEvent deserialization")
    void scoresDeletedEventDeserializationTest() throws JsonProcessingException {
        final var actualScoresDeletedEvent = this.objectMapper.readValue(EXPECTED_JSON, ScoresDeletedEvent.class);

        assertNotNull(actualScoresDeletedEvent);
        assertEquals(TEST_UUID, actualScoresDeletedEvent.getId());
    }

    @Test
    @DisplayName("Testing ScoresDeletedEvent equals() and hashCode() methods")
    void equalsAndHashCodeTest() {
        final var actualScoresDeletedEvent = createScoresDeletedEvent();

        assertTrue(this.expectedScoresDeletedEvent.equals(actualScoresDeletedEvent) &&
                actualScoresDeletedEvent.equals(this.expectedScoresDeletedEvent));
        assertEquals(this.expectedScoresDeletedEvent.hashCode(), actualScoresDeletedEvent.hashCode());
    }
}
