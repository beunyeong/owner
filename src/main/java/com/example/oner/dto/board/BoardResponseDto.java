package com.example.oner.dto.board;

import com.example.oner.dto.list.ListResponseDto;
import com.example.oner.entity.Board;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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


    public BoardResponseDto(Board board) {
        this.id = board.getId();
        this.title = board.getBoardTitle();
        this.workspaceId = board.getWorkspace().getId();
        this.backgroundColor = board.getBackgroundColor();
        this.backgroundImageUrl = board.getBackgroundImageUrl();
        this.createdAt = board.getCreatedAt();
        this.updatedAt = board.getUpdatedAt();
        this.lists = board.getLists()
                .stream()
                .map(ListResponseDto::new)
                .collect(Collectors.toList());

    }

}
