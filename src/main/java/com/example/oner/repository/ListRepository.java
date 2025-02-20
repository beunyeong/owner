package com.example.oner.repository;

import com.example.oner.entity.ListEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ListRepository extends JpaRepository<ListEntity, Long> {

    // positionList 증가
    @Modifying
    @Query("UPDATE ListEntity l SET l.positionList = l.positionList + 1 " +
            "WHERE l.board.id = :boardId AND l.positionList BETWEEN :start AND :end")
    void updatePositionIncrement(@Param("boardId") Long boardId,
                                 @Param("start") int start,
                                 @Param("end") int end);


    // positionList 감소
    @Modifying
    @Query("UPDATE ListEntity l SET l.positionList = l.positionList - 1 " +
            "WHERE l.board.id = :boardId AND l.positionList BETWEEN :start AND :end")
    void updatePositionDecrement(@Param("boardId") Long boardId,
                                 @Param("start") int start,
                                 @Param("end") int end);

    boolean existsByBoardIdAndPositionList(Long boardId, int positionList);

}

