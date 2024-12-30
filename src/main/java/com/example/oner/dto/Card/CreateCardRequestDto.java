package com.example.oner.dto.Card;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CreateCardRequestDto {

    private final Long listId;
    private final String title;
    private final String description;
    private final LocalDateTime dueDate;
    private final Long memberId;

    public CreateCardRequestDto(Long listId, String title, String description, LocalDateTime dueDate, Long memberId) {
        this.listId = listId;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.memberId = memberId;

    }

}
