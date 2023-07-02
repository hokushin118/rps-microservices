package com.al.qdt.score.qry.domain.services;

import com.al.qdt.common.api.dto.ScoreAdminDto;
import com.al.qdt.common.api.dto.ScoreDto;
import com.al.qdt.common.domain.enums.Player;

import java.util.UUID;

/**
 * Score REST API service.
 * @version 1
 */
public interface ScoreServiceV1 {

    /**
     * Returns all scores for admin users.
     *
     * @return collection of scores
     */
    Iterable<ScoreAdminDto> all();

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
    Iterable<ScoreAdminDto> findByUserId(UUID userId);

    /**
     * Find scores for admin users by winner.
     *
     * @param player winner
     * @return found scores
     */
    Iterable<ScoreAdminDto> findByWinner(Player player);

    /**
     * Find scores for admin users by user id and winner.
     *
     * @param userId user id
     * @param player winner
     * @return found scores
     */
    Iterable<ScoreAdminDto> findByUserIdAndWinner(UUID userId, Player player);

    /**
     * Find my scores.
     *
     * @param userId user id
     * @return found scores
     */
    Iterable<ScoreDto> findMyScores(UUID userId);
}
