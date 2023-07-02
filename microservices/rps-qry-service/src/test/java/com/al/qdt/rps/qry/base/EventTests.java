package com.al.qdt.rps.qry.base;

import com.al.qdt.common.infrastructure.events.rps.GamePlayedEvent;

import static com.al.qdt.common.domain.enums.Hand.ROCK;
import static com.al.qdt.common.infrastructure.helpers.Constants.TEST_UUID;
import static com.al.qdt.common.infrastructure.helpers.Constants.USER_ONE_ID;

public interface EventTests {

    /**
     * Creates test instance for game played event.
     *
     * @return game played event
     */
    default GamePlayedEvent createGamePlayedEvent() {
        final var gamePlayedEvent = GamePlayedEvent.builder()
                .userId(USER_ONE_ID)
                .hand(ROCK)
                .build();
        gamePlayedEvent.setId(TEST_UUID);
        return gamePlayedEvent;
    }
}
