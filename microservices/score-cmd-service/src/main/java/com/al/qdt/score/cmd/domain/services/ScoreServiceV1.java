package com.al.qdt.score.cmd.domain.services;

import java.util.UUID;

/**
 * Score REST API service.
 * @version 1
 */
public interface ScoreServiceV1 {

    /**
     * Deletes scores by id by admin users.
     *
     * @param id score id,
     */
    void deleteById(UUID id);
}
