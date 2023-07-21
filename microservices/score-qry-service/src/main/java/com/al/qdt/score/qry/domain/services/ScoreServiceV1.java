package com.al.qdt.score.qry.domain.services;

import com.al.qdt.common.domain.enums.Player;
import com.al.qdt.cqrs.queries.SortingOrder;
import com.al.qdt.score.qry.api.dto.ScoreAdminDto;
import com.al.qdt.score.qry.api.dto.ScoreAdminPagedResponseDto;
import com.al.qdt.score.qry.api.dto.ScorePagedResponseDto;

import java.util.UUID;

/**
 * Score REST API service.
 *
 * @version 1
 */
public interface ScoreServiceV1 {

    /**
     * Returns all scores with pagination for admin users.
     *
     * @param currentPage  current page
     * @param pageSize     page size
     * @param sortBy       sorting field
     * @param sortingOrder sorting order
     * @return collection of scores
     */
    ScoreAdminPagedResponseDto all(int currentPage, int pageSize, String sortBy, SortingOrder sortingOrder);

    /**
     * Find score for admin users by id.
     *
     * @param id score id
     * @return found score
     */
    ScoreAdminDto findById(UUID id);

    /**
     * Find scores by user id with pagination for admin users.
     *
     * @param userId       user id
     * @param currentPage  current page
     * @param pageSize     page size
     * @param sortBy       sorting field
     * @param sortingOrder sorting order
     * @return found scores
     */
    ScoreAdminPagedResponseDto findByUserId(UUID userId, int currentPage, int pageSize, String sortBy, SortingOrder sortingOrder);

    /**
     * Find scores by winner with pagination for admin users.
     *
     * @param player       winner
     * @param currentPage  current page
     * @param pageSize     page size
     * @param sortBy       sorting field
     * @param sortingOrder sorting order
     * @return found scores
     */
    ScoreAdminPagedResponseDto findByWinner(Player player, int currentPage, int pageSize, String sortBy, SortingOrder sortingOrder);

    /**
     * Find scores by user id and winner with pagination for admin users.
     *
     * @param userId       user id
     * @param player       winner
     * @param currentPage  current page
     * @param pageSize     page size
     * @param sortBy       sorting field
     * @param sortingOrder sorting order
     * @return found scores
     */
    ScoreAdminPagedResponseDto findByUserIdAndWinner(UUID userId, Player player, int currentPage, int pageSize, String sortBy, SortingOrder sortingOrder);

    /**
     * Find my scores with pagination.
     *
     * @param userId       user id
     * @param currentPage  current page
     * @param pageSize     page size
     * @param sortBy       sorting field
     * @param sortingOrder sorting order
     * @return found scores
     */
    ScorePagedResponseDto findMyScores(UUID userId, int currentPage, int pageSize, String sortBy, SortingOrder sortingOrder);
}
