package com.example.oner.dto.board;

import com.example.oner.dto.list.ListResponseDto;
import com.example.oner.entity.Board;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class BoardResponseDto {

    private Long id;

    private String title;

    private Long workspaceId;

    private String backgroundColor;

    private String backgroundImageUrl;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private List<ListResponseDto> lists;


    public BoardResponseDto(Long id, String title, Long workspaceId,
                            String backgroundColor, String backgroundImageUrl,
                            LocalDateTime createdAt, LocalDateTime updatedAt,
                            List<ListResponseDto> lists) {
        this.id = id;
        this.title = title;
        this.workspaceId = workspaceId;
        this.backgroundColor = backgroundColor;
        this.backgroundImageUrl = backgroundImageUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.lists = lists;
    }

    public BoardResponseDto(Board board) {
        this.id = board.getId();
        this.title = board.getBoardTitle();
        this.workspaceId = board.getWorkspace().getId();
        this.backgroundColor = board.getBackgroundColor();
        this.backgroundImageUrl = board.getBackgroundImageUrl();
        this.createdAt = board.getCreatedAt();
        this.updatedAt = board.getUpdatedAt();
        this.lists = null; // 기본값으로 설정
    }

}
