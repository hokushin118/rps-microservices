package com.al.qdt.rps.qry.domain.services;

import com.al.qdt.rps.qry.api.dto.GameAdminDto;
import com.al.qdt.rps.qry.api.dto.GameAdminPagedResponseDto;
import com.al.qdt.rps.qry.api.dto.GamePagedResponseDto;
import com.al.qdt.cqrs.queries.SortingOrder;

import java.util.UUID;

public interface RpsServiceV1 {

    /**
     * Returns all games with pagination for admin users.
     *
     * @param currentPage current page
     * @param pageSize page size
     * @param sortBy sorting field
     * @param sortingOrder sorting order
     * @return collection of games
     */
    @Deprecated(since = "2", forRemoval = true)
    GameAdminPagedResponseDto all(int currentPage, int pageSize, String sortBy, SortingOrder sortingOrder);

    /**
     * Find game by id for admin users.
     *
     * @param id game id
     * @return found game
     */
    @Deprecated(since = "2", forRemoval = true)
    GameAdminDto findById(UUID id);

    /**
     * Find games by user id with pagination for admin users.
     *
     * @param userId user id
     * @param currentPage current page
     * @param pageSize page size
     * @param sortBy sorting field
     * @param sortingOrder sorting order
     * @return found games
     */
    @Deprecated(since = "2", forRemoval = true)
    GameAdminPagedResponseDto findByUserId(UUID userId, int currentPage, int pageSize, String sortBy, SortingOrder sortingOrder);

    /**
     * Find my games with pagination.
     *
     * @param userId user id
     * @param currentPage current page
     * @param pageSize page size
     * @param sortBy sorting field
     * @param sortingOrder sorting order
     * @return found games
     */
    @Deprecated(since = "2", forRemoval = true)
    GamePagedResponseDto findMyGames(UUID userId, int currentPage, int pageSize, String sortBy, SortingOrder sortingOrder);
}
