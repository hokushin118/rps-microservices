package com.al.qdt.score.qry.domain.services;

import com.al.qdt.rps.grpc.v1.common.SortingOrder;
import com.al.qdt.rps.grpc.v1.common.Player;
import com.al.qdt.rps.grpc.v1.dto.ScoreAdminDto;
import com.al.qdt.rps.grpc.v1.services.ListOfScoresAdminResponse;
import com.al.qdt.rps.grpc.v1.services.ListOfScoresResponse;

import java.util.UUID;

/**
 * Score REST API service.
 *
 * @version 2
 */
public interface ScoreServiceV2 {

    /**
     * Returns all scores with pagination for admin users.
     *
     * @param currentPage  current page
     * @param pageSize     page size
     * @param sortBy       sorting field
     * @param sortingOrder sorting order
     * @return collection of scores
     */
    ListOfScoresAdminResponse all(int currentPage, int pageSize, String sortBy, SortingOrder sortingOrder);

    /**
     * Find score for admin users by id.
     *
     * @param id score id
     * @return found score
     */
    ScoreAdminDto findById(UUID id);

    /**
     * Find scores for admin users by user id.
     *
     * @param userId user id
     * @return found scores
     */
    ListOfScoresAdminResponse findByUserId(UUID userId, int currentPage, int pageSize, String sortBy, SortingOrder sortingOrder);

    /**
     * Find scores for admin users by winner.
     *
     * @param player       winner
     * @param currentPage  current page
     * @param pageSize     page size
     * @param sortBy       sorting field
     * @param sortingOrder sorting order
     * @return found scores
     */
    ListOfScoresAdminResponse findByWinner(Player player, int currentPage, int pageSize, String sortBy, SortingOrder sortingOrder);

    /**
     * Find scores for admin users by user id and winner.
     *
     * @param userId       user id
     * @param player       winner
     * @param currentPage  current page
     * @param pageSize     page size
     * @param sortBy       sorting field
     * @param sortingOrder sorting order
     * @return found scores
     */
    ListOfScoresAdminResponse findByUserIdAndWinner(UUID userId, Player player, int currentPage, int pageSize, String sortBy, SortingOrder sortingOrder);

    /**
     * Find my scores.
     *
     * @param userId       user id
     * @param currentPage  current page
     * @param pageSize     page size
     * @param sortBy       sorting field
     * @param sortingOrder sorting order
     * @return found scores
     */
    ListOfScoresResponse findMyScores(UUID userId, int currentPage, int pageSize, String sortBy, SortingOrder sortingOrder);
}
