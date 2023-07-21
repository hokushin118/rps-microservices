package com.al.qdt.score.qry.domain.repositories;

import com.al.qdt.common.domain.enums.Player;
import com.al.qdt.score.qry.domain.entities.Score;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ScoreRepository extends JpaRepository<Score, UUID> {
    Page<Score> findByUserId(UUID userId, Pageable pageable);

    Page<Score> findByWinner(Player winner, Pageable pageable);

    Page<Score> findByUserIdAndWinner(UUID userId, Player winner, Pageable pageable);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "DELETE FROM score WHERE id = :id", nativeQuery = true)
    void deleteById(@Param("id") UUID id);
}
