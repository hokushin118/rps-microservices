package com.al.qdt.rps.qry.base;

import com.al.qdt.common.api.dto.PagingDto;
import com.al.qdt.rps.qry.api.dto.GameAdminDto;
import com.al.qdt.rps.qry.api.dto.GameAdminPagedResponseDto;
import com.al.qdt.rps.qry.api.dto.GameDto;
import com.al.qdt.rps.qry.api.dto.GamePagedResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.UUID;

import static com.al.qdt.common.domain.enums.Hand.ROCK;
import static com.al.qdt.common.infrastructure.helpers.Constants.TEST_ID;
import static com.al.qdt.common.infrastructure.helpers.Constants.TEST_UUID;
import static com.al.qdt.common.infrastructure.helpers.Utils.createObjectMapper;

public interface DtoTests {
    int CURRENT_PAGE = 1;
    int PAGE_SIZE = 10;
    long TOTAL_ELEMENTS = 1;

    ObjectMapper objectMapper = createObjectMapper();

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
     * Creates test instance for game dto object without userId.
     *
     * @return game dto object
     */
    default GameDto createGameDtoWithMissedUserId() {
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
     * Creates test instance for game response dto object.
     *
     * @return game response dto object
     */
    default GamePagedResponseDto createGamePagedResponseDto() {
        return GamePagedResponseDto.builder()
                .games(List.of(createGameDto(TEST_UUID)))
                .paging(createPagingDto())
                .build();
    }

    /**
     * Creates test instance for game admin response dto object.
     *
     * @return game admin response dto object
     */
    default GameAdminPagedResponseDto createGameAdminPagedResponseDto() {
        return GameAdminPagedResponseDto.builder()
                .games(List.of(createGameAdminDto(TEST_UUID)))
                .paging(createPagingDto())
                .build();
    }

    /**
     * Creates test instance for paging dto object.
     *
     * @return paging dto object
     */
    default PagingDto createPagingDto() {
        return PagingDto.builder()
                .currentPage(CURRENT_PAGE)
                .pageSize(PAGE_SIZE)
                .totalElements(TOTAL_ELEMENTS)
                .build();
    }
}
