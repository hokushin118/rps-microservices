package com.al.qdt.rps.qry.domain.services;

import com.al.qdt.common.api.dto.GameAdminDto;
import com.al.qdt.common.api.dto.GameDto;

import java.util.UUID;

public interface RpsServiceV1 {

    /**
     * Returns all games for admin users.
     *
     * @return collection of games
     */
    Iterable<GameAdminDto> all();

    /**
     * Find game by id for admin users.
     *
     * @param id game id
     * @return found game
     */
    GameAdminDto findById(UUID id);

    /**
     * Find games by user id for admin users.
     *
     * @param userId user id
     * @return found games
     */
    Iterable<GameAdminDto> findByUserId(UUID userId);

    /**
     * Find my games.
     *
     * @param userId user id
     * @return found games
     */
    Iterable<GameDto> findMyGames(UUID userId);
}
