package com.al.qdt.rps.qry.domain.services;

import com.al.qdt.rps.grpc.v1.dto.GameAdminDto;
import com.al.qdt.rps.grpc.v1.dto.GameDto;
import com.al.qdt.rps.grpc.v1.services.ListOfGamesAdminResponse;
import com.al.qdt.rps.grpc.v1.services.ListOfGamesResponse;

import java.util.UUID;

public interface RpsServiceV2 {

    /**
     * Returns all games for admin users.
     *
     * @return collection of games
     */
    ListOfGamesAdminResponse all();

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
    ListOfGamesAdminResponse findByUserId(UUID userId);

    /**
     * Find my games.
     *
     * @param userId user id
     * @return found games
     */
    ListOfGamesResponse findMyGames(UUID userId);
}
