package com.al.qdt.rps.cmd.base;

import com.al.qdt.common.domain.enums.Hand;
import com.al.qdt.common.domain.enums.Player;
import com.al.qdt.rps.cmd.api.commands.AddScoreCommand;
import com.al.qdt.rps.cmd.api.commands.DeleteGameCommand;
import com.al.qdt.rps.cmd.api.commands.PlayGameCommand;
import com.al.qdt.rps.cmd.api.commands.RestoreDbCommand;

import java.util.UUID;

public interface CommandTests {

    /**
     * Creates test instance for play game command object.
     *
     * @param id     id of the command
     * @param userId user id
     * @param hand   user choice
     * @return play game command object
     */
    default PlayGameCommand createPlayGameCommand(UUID id, UUID userId, Hand hand) {
        return PlayGameCommand.builder()
                .id(id)
                .userId(userId)
                .hand(hand)
                .build();
    }

    /**
     * Creates test instance for add score command object.
     *
     * @param id     id of the command
     * @param userId user id
     * @param winner winner of the round
     * @return add score command object
     */
    default AddScoreCommand createAddScoreCommand(UUID id, UUID userId, Player winner) {
        return AddScoreCommand.builder()
                .id(id)
                .userId(userId)
                .winner(winner)
                .build();
    }

    /**
     * Creates test instance for delete game command object.
     *
     * @param id     id of the command
     * @param userId user id
     * @return delete game command object
     */
    default DeleteGameCommand createDeleteGameCommand(UUID id, UUID userId) {
        return DeleteGameCommand.builder()
                .id((id))
                .userId(userId)
                .build();
    }

    /**
     * Creates test instance for restore database command object.
     *
     * @param id     id of the command
     * @param userId user id
     * @return restore database command object
     */
    default RestoreDbCommand createRestoreDbCommand(UUID id, UUID userId) {
        return RestoreDbCommand.builder()
                .id(id)
                .userId(userId)
                .build();
    }
}
