package com.al.qdt.rps.qry.api.dto;

import com.al.qdt.rps.qry.base.DtoTests;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.JSONException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import static com.al.qdt.common.infrastructure.helpers.Constants.GAME_DTO_RESPONSE_EXPECTED_JSON;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Testing GamePagedResponseDto class")
class GamePagedResponseDtoTest implements DtoTests {
    GamePagedResponseDto expectedGamePagedResponseDto;

    @BeforeEach
    void setUp() {
        this.expectedGamePagedResponseDto = createGamePagedResponseDto();
    }

    @AfterEach
    void tearDown() {
        this.expectedGamePagedResponseDto = null;
    }

    @Test
    @DisplayName("Testing GamePagedResponseDto properties")
    void gamePagedResponseDtoPropertiesTest() {
        assertAll("Testing GamePagedResponseDto",
                () -> assertEquals(1, this.expectedGamePagedResponseDto.getGames().size(), "Id didn't match!"),
                () -> assertEquals(CURRENT_PAGE, this.expectedGamePagedResponseDto.getPaging().getCurrentPage(), "Current page didn't match!"),
                () -> assertEquals(PAGE_SIZE, this.expectedGamePagedResponseDto.getPaging().getPageSize(), "Page size didn't match!"),
                () -> assertEquals(TOTAL_ELEMENTS, this.expectedGamePagedResponseDto.getPaging().getTotalElements(), "Total elements didn't match!")
        );
    }

    @Test
    @DisplayName("Testing GamePagedResponseDto serialization")
    void gamePagedResponseDtoSerializationTest() throws JsonProcessingException, JSONException {
        final var actualJson = objectMapper.writeValueAsString(this.expectedGamePagedResponseDto);

        assertNotNull(actualJson);
        assertThat(actualJson, containsString("games"));
        assertThat(actualJson, containsString("paging"));

        JSONAssert.assertEquals(GAME_DTO_RESPONSE_EXPECTED_JSON, actualJson, true);
    }

    @Test
    @DisplayName("Testing GamePagedResponseDto deserialization")
    void gamePagedResponseDtoDeserializationTest() throws JsonProcessingException {
        final var actualGamePagedResponseDto = objectMapper.readValue(GAME_DTO_RESPONSE_EXPECTED_JSON, GamePagedResponseDto.class);

        assertNotNull(actualGamePagedResponseDto);
        assertEquals(1, actualGamePagedResponseDto.getGames().size());
        assertEquals(CURRENT_PAGE, actualGamePagedResponseDto.getPaging().getCurrentPage());
        assertEquals(this.expectedGamePagedResponseDto.getPaging().getCurrentPage(), actualGamePagedResponseDto.getPaging().getCurrentPage());
        assertEquals(PAGE_SIZE, actualGamePagedResponseDto.getPaging().getPageSize());
        assertEquals(this.expectedGamePagedResponseDto.getPaging().getPageSize(), actualGamePagedResponseDto.getPaging().getPageSize());
        assertEquals(TOTAL_ELEMENTS, actualGamePagedResponseDto.getPaging().getTotalElements());
        assertEquals(this.expectedGamePagedResponseDto.getPaging().getTotalElements(), actualGamePagedResponseDto.getPaging().getTotalElements());
    }

    @Test
    @DisplayName("Testing GamePagedResponseDto equals() and hashCode() methods")
    void equalsAndHashCodeTest() {
        final var actualGamePagedResponseDto = createGamePagedResponseDto();

        assertTrue(this.expectedGamePagedResponseDto.equals(actualGamePagedResponseDto) &&
                actualGamePagedResponseDto.equals(this.expectedGamePagedResponseDto));
        assertEquals(this.expectedGamePagedResponseDto.hashCode(), actualGamePagedResponseDto.hashCode());
    }
}
