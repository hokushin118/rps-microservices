package com.al.qdt.rps.qry.base;

import com.al.qdt.rps.grpc.v1.dto.GameAdminDto;
import com.al.qdt.rps.grpc.v1.dto.GameDto;

import java.util.UUID;

import static com.al.qdt.rps.grpc.v1.common.Hand.ROCK;

public interface ProtoTests {

    /**
     * Creates test proto3 message for a game.
     *
     * @param userId user id
     * @return proto3 message
     */
    default GameDto createGameDto(UUID userId) {
        return GameDto.newBuilder()
                .setHand(ROCK)
                .build();
    }

    /**
     * Creates test proto3 message for a game admin.
     *
     * @param userId user id
     * @return proto3 message
     */
    default GameAdminDto createGameAdminDto(UUID userId) {
        return GameAdminDto.newBuilder()
                .setUserId(userId.toString())
                .setHand(ROCK)
                .build();
    }
}
