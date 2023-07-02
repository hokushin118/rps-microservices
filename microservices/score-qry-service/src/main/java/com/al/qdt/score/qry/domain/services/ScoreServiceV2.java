package com.al.qdt.score.qry.domain.services;

import com.al.qdt.rps.grpc.v1.common.Player;
import com.al.qdt.rps.grpc.v1.dto.ScoreAdminDto;
import com.al.qdt.rps.grpc.v1.services.ListOfScoresAdminResponse;
import com.al.qdt.rps.grpc.v1.services.ListOfScoresResponse;

import java.util.UUID;

/**
 * Score REST API service.
 * @version 2
 */
public interface ScoreServiceV2 {

    /**
     * Returns all scores for admin users.
     *
     * @return collection of scores
     */
    ListOfScoresAdminResponse all();

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
    ListOfScoresAdminResponse findByUserId(UUID userId);

    /**
     * Find scores for admin users by winner.
     *
     * @param player winner
     * @return found scores
     */
    ListOfScoresAdminResponse findByWinner(Player player);

    /**
     * Find scores for admin users by user id and winner.
     *
     * @param userId user id
     * @param player winner
     * @return found scores
     */
    ListOfScoresAdminResponse findByUserIdAndWinner(UUID userId, Player player);

    /**
     * Find my scores.
     *
     * @param userId user id
     * @return found scores
     */
    ListOfScoresResponse findMyScores(UUID userId);
}
