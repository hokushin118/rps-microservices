package com.al.qdt.score.qry.base

import com.al.qdt.rps.grpc.v1.common.Player
import com.al.qdt.rps.grpc.v1.dto.ScoreAdminDto
import com.al.qdt.rps.grpc.v1.dto.ScoreDto

import static com.al.qdt.rps.grpc.v1.common.Player.USER

trait ProtoTests {

    /**
     * Creates test instance for score proto object.
     *
     * @return score proto object
     */
    ScoreDto createScoreProtoDto() {
        ScoreDto.newBuilder()
                .setWinner(USER.name())
                .build()
    }

    /**
     * Creates test instance for score proto object for admin users.
     *
     * @param id score id
     * @param userId user id
     * @param winner round winner
     * @return score proto object
     */
    ScoreAdminDto createScoreAdminProtoDto(UUID id, UUID userId, Player winner) {
        ScoreAdminDto.newBuilder()
                .setId(id.toString())
                .setUserId(userId.toString())
                .setWinner(winner.name())
                .build()
    }
}
