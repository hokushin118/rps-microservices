package com.al.qdt.score.qry.base

import com.al.qdt.common.infrastructure.events.score.ScoresAddedEvent

import static com.al.qdt.common.domain.enums.Player.USER
import static com.al.qdt.common.infrastructure.helpers.Constants.TEST_UUID
import static com.al.qdt.common.infrastructure.helpers.Constants.USER_ONE_ID

trait EventTests {

    /**
     * Creates test instance for scores added event.
     *
     * @return scores added event
     */
    ScoresAddedEvent createScoresAddedEvent() {
        ScoresAddedEvent.builder()
                .id(TEST_UUID)
                .userId(USER_ONE_ID)
                .winner(USER)
                .build()
    }
}
