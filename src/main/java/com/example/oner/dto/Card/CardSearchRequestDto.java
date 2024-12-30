package com.example.oner.dto.Card;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CardSearchRequestDto {
    private String title;
    private String description;
    private LocalDateTime dueDateFrom;
    private LocalDateTime dueDateTo;
    private Long listId;
    private Long boardId;
    private Long minViews; // 조회수 최소값
    private int page = 0;
    private int size = 10;
}
