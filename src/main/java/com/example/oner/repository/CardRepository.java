package com.example.oner.repository;

import com.example.oner.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long>, JpaSpecificationExecutor<Card> {

    @Query("SELECT c FROM Card c JOIN c.list l JOIN l.board b WHERE b.workspace.id = :workspaceId")
    List<Card> findByWorkspaceId(@Param("workspaceId") Long workspaceId);

}
