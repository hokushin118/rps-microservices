package com.al.qdt.rps.qry.api.dto;

import com.al.qdt.rps.qry.base.DtoTests;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.JSONException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import static com.al.qdt.common.infrastructure.helpers.Constants.GAME_DTO_ADMIN_RESPONSE_EXPECTED_JSON;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Testing GameAdminPagedResponseDto class")
class GameAdminPagedResponseDtoTest implements DtoTests {
    GameAdminPagedResponseDto expectedGameAdminPagedResponseDto;

    @BeforeEach
    void setUp() {
        this.expectedGameAdminPagedResponseDto = createGameAdminPagedResponseDto();
    }

    @AfterEach
    void tearDown() {
        this.expectedGameAdminPagedResponseDto = null;
    }

    @Test
    @DisplayName("Testing GameAdminPagedResponseDto properties")
    void gameAdminPagedResponseDtoPropertiesTest() {
        assertAll("Testing GameAdminPagedResponseDto",
                () -> assertEquals(1, this.expectedGameAdminPagedResponseDto.getGames().size(), "Id didn't match!"),
                () -> assertEquals(CURRENT_PAGE, this.expectedGameAdminPagedResponseDto.getPaging().getCurrentPage(), "Current page didn't match!"),
                () -> assertEquals(PAGE_SIZE, this.expectedGameAdminPagedResponseDto.getPaging().getPageSize(), "Page size didn't match!"),
                () -> assertEquals(TOTAL_ELEMENTS, this.expectedGameAdminPagedResponseDto.getPaging().getTotalElements(), "Total elements didn't match!")
        );
    }

    @Test
    @DisplayName("Testing GameAdminPagedResponseDto serialization")
    void gameAdminPagedResponseDtoSerializationTest() throws JsonProcessingException, JSONException {
        final var actualJson = objectMapper.writeValueAsString(this.expectedGameAdminPagedResponseDto);

        assertNotNull(actualJson);
        assertThat(actualJson, containsString("games"));
        assertThat(actualJson, containsString("paging"));

        JSONAssert.assertEquals(GAME_DTO_ADMIN_RESPONSE_EXPECTED_JSON, actualJson, true);
    }

    @Test
    @DisplayName("Testing GameAdminPagedResponseDto deserialization")
    void gameAdminPagedResponseDtoDeserializationTest() throws JsonProcessingException {
        final var actualGameAdminPagedResponseDto = objectMapper.readValue(GAME_DTO_ADMIN_RESPONSE_EXPECTED_JSON, GameAdminPagedResponseDto.class);

        assertNotNull(actualGameAdminPagedResponseDto);
        assertEquals(1, actualGameAdminPagedResponseDto.getGames().size());
        assertEquals(CURRENT_PAGE, actualGameAdminPagedResponseDto.getPaging().getCurrentPage());
        assertEquals(this.expectedGameAdminPagedResponseDto.getPaging().getCurrentPage(), actualGameAdminPagedResponseDto.getPaging().getCurrentPage());
        assertEquals(PAGE_SIZE, actualGameAdminPagedResponseDto.getPaging().getPageSize());
        assertEquals(this.expectedGameAdminPagedResponseDto.getPaging().getPageSize(), actualGameAdminPagedResponseDto.getPaging().getPageSize());
        assertEquals(TOTAL_ELEMENTS, actualGameAdminPagedResponseDto.getPaging().getTotalElements());
        assertEquals(this.expectedGameAdminPagedResponseDto.getPaging().getTotalElements(), actualGameAdminPagedResponseDto.getPaging().getTotalElements());
    }

    @Test
    @DisplayName("Testing GameAdminPagedResponseDto equals() and hashCode() methods")
    void equalsAndHashCodeTest() {
        final var actualGameAdminPagedResponseDto = createGameAdminPagedResponseDto();

        assertTrue(this.expectedGameAdminPagedResponseDto.equals(actualGameAdminPagedResponseDto) &&
                actualGameAdminPagedResponseDto.equals(this.expectedGameAdminPagedResponseDto));
        assertEquals(this.expectedGameAdminPagedResponseDto.hashCode(), actualGameAdminPagedResponseDto.hashCode());
    }
}