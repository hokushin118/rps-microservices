package com.al.qdt.rps.qry.base;

import com.al.qdt.common.domain.enums.Hand;
import com.al.qdt.rps.qry.domain.entities.Game;

import java.util.UUID;

import static com.al.qdt.common.domain.enums.Hand.ROCK;
import static com.al.qdt.common.infrastructure.helpers.Constants.TEST_UUID_TWO;
import static com.al.qdt.common.infrastructure.helpers.Constants.USER_ONE_ID;
import static com.al.qdt.common.infrastructure.helpers.Constants.USER_TWO_ID;

public interface EntityTests {

    /**
     * Creates test instance for game entity object.
     *
     * @param id     game id
     * @param userId user id
     * @param hand   user choice
     * @return game entity object
     */
    default Game createGame(UUID id, UUID userId, Hand hand) {
        final var game = Game.builder()
                .userId(userId)
                .hand(hand)
                .build();
        game.setId(id);
        return game;
    }

    /**
     * Creates test instance for game entity object.
     *
     * @return game entity object
     */
    default Game createSecondGame() {
        final var game = Game.builder()
                .userId(USER_TWO_ID)
                .hand(ROCK)
                .build();
        game.setId(TEST_UUID_TWO);
        return game;
    }

    /**
     * Creates test instance for game entity object with null id.
     *
     * @return game entity object
     */
    default Game createGameWithNulUUID() {
        return Game.builder()
                .userId(USER_ONE_ID)
                .hand(ROCK)
                .build();
    }
}
