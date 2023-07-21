package com.al.qdt.score.qry.base

import com.al.qdt.score.qry.api.dto.ScoreAdminDto
import com.al.qdt.score.qry.api.dto.ScoreDto
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach

import static com.al.qdt.common.domain.enums.Player.USER
import static com.al.qdt.common.infrastructure.helpers.Constants.TEST_ID
import static com.al.qdt.common.infrastructure.helpers.Constants.USER_ONE_ID
import static com.al.qdt.common.infrastructure.helpers.Utils.createObjectMapper

abstract class DtoTests {
    protected ObjectMapper objectMapper

    @BeforeEach
    void setUp() {
        objectMapper = createObjectMapper()
    }

    /**
     * Creates test instance for score dto object.
     *
     * @return score dto object
     */
    ScoreDto createScoreDto() {
        return ScoreDto.builder()
                .id(TEST_ID)
                .winner(USER.name())
                .build();
    }

    /**
     * Creates test instance for score admin dto object.
     *
     * @return score admin dto object
     */
    ScoreAdminDto createScoreAdminDto() {
        return ScoreAdminDto.builder()
                .id(TEST_ID)
                .userId(USER_ONE_ID.toString())
                .winner(USER.name())
                .build();
    }
}
