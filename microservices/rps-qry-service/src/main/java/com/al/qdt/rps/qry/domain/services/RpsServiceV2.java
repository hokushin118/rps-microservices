package com.al.qdt.rps.qry.domain.services;

import com.al.qdt.rps.grpc.v1.common.SortingOrder;
import com.al.qdt.rps.grpc.v1.dto.GameAdminDto;
import com.al.qdt.rps.grpc.v1.services.ListOfGamesAdminResponse;
import com.al.qdt.rps.grpc.v1.services.ListOfGamesResponse;

import java.util.UUID;

public interface RpsServiceV2 {

    /**
     * Returns all games with pagination for admin users.
     *
     * @param currentPage  current page
     * @param pageSize     page size
     * @param sortBy       sorting field
     * @param sortingOrder sorting order
     * @return collection of games
     */
    ListOfGamesAdminResponse all(int currentPage, int pageSize, String sortBy, SortingOrder sortingOrder);

    /**
     * Find game by id for admin users.
     *
     * @param id game id
     * @return found game
     */
    GameAdminDto findById(UUID id);

    /**
     * Find games by user id with pagination for admin users.
     *
     * @param userId       user id
     * @param currentPage  current page
     * @param pageSize     page size
     * @param sortBy       sorting field
     * @param sortingOrder sorting order
     * @return found games
     */
    ListOfGamesAdminResponse findByUserId(UUID userId, int currentPage, int pageSize, String sortBy, SortingOrder sortingOrder);

    /**
     * Find my games with pagination.
     *
     * @param userId       user id
     * @param currentPage  current page
     * @param pageSize     page size
     * @param sortBy       sorting field
     * @param sortingOrder sorting order
     * @return found games
     */
    ListOfGamesResponse findMyGames(UUID userId, int currentPage, int pageSize, String sortBy, SortingOrder sortingOrder);
}
