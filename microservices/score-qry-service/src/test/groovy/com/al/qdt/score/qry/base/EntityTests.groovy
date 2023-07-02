package com.al.qdt.score.qry.base

import com.al.qdt.common.domain.enums.Player
import com.al.qdt.score.qry.domain.entities.Score

import static com.al.qdt.common.infrastructure.helpers.Constants.TEST_UUID_TWO
import static com.al.qdt.common.domain.enums.Player.USER
import static com.al.qdt.common.infrastructure.helpers.Constants.USER_ONE_ID
import static com.al.qdt.common.infrastructure.helpers.Constants.USER_TWO_ID

trait EntityTests {

    /**
     * Creates test instance for score entity object.
     *
     * @param id game id
     * @param userId user id
     * @param winner winner of the game
     * @return score entity object
     */
    Score createScore(UUID id, UUID userId, Player winner) {
        new Score(id: id, userId: userId, winner: winner)
    }

    /**
     * Creates test instance for score entity object.
     *
     * @return score entity object
     */
    Score createSecondScore() {
        new Score(id: TEST_UUID_TWO, userId: USER_TWO_ID, winner: USER)
    }

    /**
     * Creates test instance with nullable id for score entity object.
     *
     * @return score entity object
     */
    Score createScoreWithNulUUID() {
        new Score(id: null, userId: USER_ONE_ID, winner: USER)
    }
}
