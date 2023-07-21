package com.al.qdt.rps.cmd.base;

import com.al.qdt.rps.cmd.api.dto.BaseResponseDto;
import com.al.qdt.rps.cmd.api.dto.GameResponseDto;

import static com.al.qdt.common.domain.enums.Hand.ROCK;
import static com.al.qdt.common.domain.enums.Hand.SCISSORS;
import static com.al.qdt.common.domain.enums.Player.USER;
import static com.al.qdt.common.infrastructure.helpers.Constants.SUCCESS_MESSAGE;

public interface DtoTests {

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
}
