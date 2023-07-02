package com.al.qdt.score.cmd.domain.services;

import java.util.UUID;

/**
 * Score REST API service.
 * @version 2
 */
public interface ScoreServiceV2 {

    /**
     * Deletes scores by id by admin users.
     *
     * @param id score id,
     */
    void deleteById(UUID id);
}
