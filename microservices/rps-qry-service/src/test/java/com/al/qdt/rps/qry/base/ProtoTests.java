package com.al.qdt.rps.qry.base;

import com.al.qdt.rps.grpc.v1.dto.GameAdminDto;
import com.al.qdt.rps.grpc.v1.dto.GameDto;

import java.util.UUID;

import static com.al.qdt.common.infrastructure.helpers.Constants.TEST_ID;
import static com.al.qdt.common.infrastructure.helpers.Constants.USER_ONE_ID;
import static com.al.qdt.rps.grpc.v1.common.Hand.ROCK;

public interface ProtoTests {

    /**
     * Creates test proto3 message for a game.
     *
     * @param userId user id
     * @return proto3 message
     */
    default GameDto createGameProtoDto(UUID userId) {
        return GameDto.newBuilder()
                .setId(TEST_ID)
                .setHand(ROCK)
                .build();
    }

    /**
     * Creates test proto3 message for a game admin.
     *
     * @param userId user id
     * @return proto3 message
     */
    default GameAdminDto createGameAdminProtoDto(UUID userId) {
        return GameAdminDto.newBuilder()
                .setId(TEST_ID)
                .setUserId(userId.toString())
                .setHand(ROCK)
                .build();
    }
}
