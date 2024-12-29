package com.example.oner.repository;

import com.example.oner.entity.Board;
import com.example.oner.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    // 여러 워크스페이스에 속한 보드 목록 조회
    List<Board> findAllByWorkspaceIn(List<Workspace> workspaces);
}

