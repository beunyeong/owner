package com.example.oner.dto.Card;

import lombok.Getter;

import java.time.LocalDateTime;
@Getter
public class UpdateCardRequestDto {
    private final Long listId;
    private final Long memberId;
    private final String title;
    private final String description;
    private final LocalDateTime dueDate;

    public UpdateCardRequestDto(Long listId, Long memberId, String title, String description, LocalDateTime dueDate) {
        this.listId = listId;
        this.memberId = memberId;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
    }
}
