package com.al.qdt.rps.cmd.base;

import com.al.qdt.common.api.dto.BaseResponseDto;
import com.al.qdt.common.api.dto.GameAdminDto;
import com.al.qdt.common.api.dto.GameDto;
import com.al.qdt.common.api.dto.GameResponseDto;

import java.util.UUID;

import static com.al.qdt.common.domain.enums.Hand.ROCK;
import static com.al.qdt.common.domain.enums.Hand.SCISSORS;
import static com.al.qdt.common.domain.enums.Player.USER;
import static com.al.qdt.common.infrastructure.helpers.Constants.SUCCESS_MESSAGE;
import static com.al.qdt.common.infrastructure.helpers.Constants.TEST_ID;

public interface DtoTests {

    /**
     * Creates test instance for base response dto object.
     *
     * @return base response dto object
     */
    default BaseResponseDto createBaseResponse() {
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
     * @param userId user id
     * @return game dto object
     */
    default GameDto createGameDto(UUID userId) {
        return GameDto.builder()
                .id(TEST_ID)
                .hand(ROCK.name())
                .build();
    }

    /**
     * Creates test instance for game admin dto object.
     *
     * @param userId user id
     * @return game admin dto object
     */
    default GameAdminDto createGameAdminDto(UUID userId) {
        return GameAdminDto.builder()
                .id(TEST_ID)
                .userId(userId.toString())
                .hand(ROCK.name())
                .build();
    }

    /**
     * Creates test instance for game dto object without userId.
     *
     * @return game dto object
     */
    default GameDto createGameDtoWithMissedUserId() {
        return GameDto.builder()
                .hand(ROCK.name())
                .build();
    }
}
