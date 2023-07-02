package com.al.qdt.common.api.dto;

import com.al.qdt.common.domain.base.DtoTests;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.JSONException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import static com.al.qdt.common.infrastructure.helpers.Constants.BASE_RESPONSE_EXPECTED_JSON;
import static com.al.qdt.common.infrastructure.helpers.Constants.SUCCESS_MESSAGE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Testing BaseResponseDto class")
class BaseResponseDtoTest implements DtoTests {
    BaseResponseDto expectedBaseResponseDto;

    @BeforeEach
    void setUp() {
        this.expectedBaseResponseDto = createBaseResponseDto();
    }

    @AfterEach
    void tearDown() {
        this.expectedBaseResponseDto = null;
    }

    @Test
    @DisplayName("Testing BaseResponseDto serialization")
    void baseResponseDtoSerializationTest() throws JsonProcessingException, JSONException {
        final var actualJson = objectMapper.writeValueAsString(this.expectedBaseResponseDto);

        assertNotNull(actualJson);
        assertThat(actualJson, containsString("message"));
        assertThat(actualJson, not(containsString("score")));

        JSONAssert.assertEquals(BASE_RESPONSE_EXPECTED_JSON, actualJson, true);
    }

    @Test
    @DisplayName("Testing BaseResponseDto deserialization")
    void baseResponseDtoDeserializationTest() throws JsonProcessingException {
        final var actualBaseResponseDto = objectMapper.readValue(BASE_RESPONSE_EXPECTED_JSON, BaseResponseDto.class);

        assertNotNull(actualBaseResponseDto);
        assertEquals(SUCCESS_MESSAGE, actualBaseResponseDto.getMessage());
    }

    @Test
    @DisplayName("Testing BaseResponseDto equals() and hashCode() methods")
    void equalsAndHashCodeTest() {
        final var actualBaseResponse = createBaseResponseDto();

        assertTrue(this.expectedBaseResponseDto.equals(actualBaseResponse) &&
                actualBaseResponse.equals(this.expectedBaseResponseDto));
        assertEquals(this.expectedBaseResponseDto.hashCode(), actualBaseResponse.hashCode());
    }
}
