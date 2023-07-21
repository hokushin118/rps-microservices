package com.al.qdt.rps.qry.domain.repositories;

import com.al.qdt.rps.qry.domain.entities.Game;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface GameRepository extends JpaRepository<Game, UUID> {
    Page<Game> findAll(Pageable pageable);

    Page<Game> findByUserId(UUID userId, Pageable pageable);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "DELETE FROM game WHERE id = :id", nativeQuery = true)
    void deleteById(@Param("id") UUID id);
}
