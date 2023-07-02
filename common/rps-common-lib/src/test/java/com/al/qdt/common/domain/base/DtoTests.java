package com.al.qdt.common.domain.base;

import com.al.qdt.common.api.dto.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.junit.jupiter.api.Tag;

import static com.al.qdt.common.domain.enums.Hand.ROCK;
import static com.al.qdt.common.domain.enums.Hand.SCISSORS;
import static com.al.qdt.common.domain.enums.Player.USER;
import static com.al.qdt.common.infrastructure.helpers.Constants.SUCCESS_MESSAGE;
import static com.al.qdt.common.infrastructure.helpers.Constants.TEST_ID;
import static com.al.qdt.common.infrastructure.helpers.Constants.USER_ONE_ID;

@Tag(value = "dto")
public interface DtoTests {
    ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new ParameterNamesModule())
            .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);

    /**
     * Creates test instance for base response dto object.
     *
     * @return base response dto object
     */
    default BaseResponseDto createBaseResponseDto() {
        return BaseResponseDto.builder()
                .message(SUCCESS_MESSAGE)
                .build();
    }

    /**
     * Creates test instance for game response dto object.
     *
     * @return game response dto object
     */
    default GameResponseDto createGameResponseDto() {
        return GameResponseDto.builder()
                .userChoice(ROCK.name())
                .machineChoice(SCISSORS.name())
                .result(USER)
                .build();
    }

    /**
     * Creates test instance for game dto object.
     *
     * @return game dto object
     */
    default GameDto createGameDto() {
        return GameDto.builder()
                .id(TEST_ID)
                .hand(ROCK.name())
                .build();
    }

    /**
     * Creates test instance for game admin dto object.
     *
     * @return game admin dto object
     */
    default GameAdminDto createGameAdminDto() {
        return GameAdminDto.builder()
                .id(TEST_ID)
                .userId(USER_ONE_ID.toString())
                .hand(ROCK.name())
                .build();
    }

    /**
     * Creates test instance for score dto object.
     *
     * @return score dto object
     */
    default ScoreDto createScoreDto() {
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
    default ScoreAdminDto createScoreAdminDto() {
        return ScoreAdminDto.builder()
                .id(TEST_ID)
                .userId(USER_ONE_ID.toString())
                .winner(USER.name())
                .build();
    }
}
