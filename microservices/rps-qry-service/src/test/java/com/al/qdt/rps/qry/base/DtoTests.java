package com.al.qdt.rps.qry.base;

import com.al.qdt.common.api.dto.GameAdminDto;
import com.al.qdt.common.api.dto.GameDto;

import java.util.UUID;

import static com.al.qdt.common.domain.enums.Hand.ROCK;

public interface DtoTests {

    /**
     * Creates test instance for game dto object.
     *
     * @param userId user id
     * @return game dto object
     */
    default GameDto createGameDto(UUID userId) {
        return GameDto.builder()
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
                .userId(userId.toString())
                .hand(ROCK.name())
                .build();
    }
}
