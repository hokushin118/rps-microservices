package com.al.qdt.rps.cmd.base;

import com.al.qdt.rps.grpc.v1.common.BaseResponseDto;
import com.al.qdt.rps.grpc.v1.common.Hand;
import com.al.qdt.rps.grpc.v1.dto.GameAdminDto;
import com.al.qdt.rps.grpc.v1.dto.GameDto;
import com.al.qdt.rps.grpc.v1.dto.GameResultDto;
import com.al.qdt.rps.grpc.v1.services.GameRequest;

import static com.al.qdt.common.infrastructure.helpers.Constants.SUCCESS_MESSAGE;
import static com.al.qdt.common.infrastructure.helpers.Constants.TEST_ID;
import static com.al.qdt.common.infrastructure.helpers.Constants.USER_ONE_ID;
import static com.al.qdt.rps.grpc.v1.common.Hand.ROCK;
import static com.al.qdt.rps.grpc.v1.common.Hand.SCISSORS;
import static com.al.qdt.rps.grpc.v1.common.Player.USER;

public interface ProtoTests {

    /**
     * Creates test proto3 message for base response.
     *
     * @return proto3 message
     */
    default BaseResponseDto createBaseResponseProtoDto() {
        return BaseResponseDto.newBuilder()
                .setMessage(SUCCESS_MESSAGE)
                .build();
    }

    /**
     * Creates test proto3 message for base response.
     *
     * @return proto3 message
     */
    default BaseResponseDto createBaseResponseDto() {
        return BaseResponseDto.newBuilder()
                .setMessage(SUCCESS_MESSAGE)
                .build();
    }

    /**
     * Creates test proto3 message for game result.
     *
     * @return proto3 message
     */
    default GameResultDto createGameResultDto() {
        return GameResultDto.newBuilder()
                .setUserChoice(ROCK.name())
                .setMachineChoice(SCISSORS.name())
                .setResult(USER.name())
                .build();
    }

    /**
     * Creates test proto3 message for a game.
     *
     * @return proto3 message
     */
    default GameDto createGameDto() {
        return GameDto.newBuilder()
                .setId(TEST_ID)
                .setHand(ROCK)
                .build();
    }

    /**
     * Creates test proto3 message for a game admin.
     *
     * @return proto3 message
     */
    default GameAdminDto createGameAdminDto() {
        return GameAdminDto.newBuilder()
                .setId(TEST_ID)
                .setUserId(USER_ONE_ID.toString())
                .setHand(ROCK)
                .build();
    }

    /**
     * Creates test proto3 message for a game request.
     *
     * @return proto3 message
     */
    default GameRequest createGameRequest(Hand hand) {
        return GameRequest.newBuilder()
                .setHand(hand)
                .build();
    }

    /**
     * Creates test proto3 message for game result.
     *
     * @return proto3 message
     */
    default GameResultDto createGameResultProtoDto() {
        return GameResultDto.newBuilder()
                .setUserChoice(ROCK.name())
                .setMachineChoice(SCISSORS.name())
                .setResult(USER.name())
                .build();
    }
}
