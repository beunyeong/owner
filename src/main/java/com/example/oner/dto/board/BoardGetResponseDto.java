package com.example.oner.dto.board;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoardGetResponseDto {

    private Long id;

    private String title;

    private Long workspaceId;

    private String backgroundColor;

    private String backgroundImageUrl;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public BoardGetResponseDto(Long id, String title, Long workspaceId,
                            String backgroundColor, String backgroundImageUrl,
                            LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.workspaceId = workspaceId;
        this.backgroundColor = backgroundColor;
        this.backgroundImageUrl = backgroundImageUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

}
