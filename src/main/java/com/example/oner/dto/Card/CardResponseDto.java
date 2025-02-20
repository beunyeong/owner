package com.example.oner.dto.Card;

import lombok.Getter;

@Getter
public class CardResponseDto {
    private final Long id;
    private final Long listId;
    private final Long memberId;
    private final String title;
    private final String description;

    public CardResponseDto(Long id, Long listId, Long memberId, String title, String description) {
        this.id = id;
        this.listId = listId;
        this.memberId = memberId;
        this.title = title;
        this.description = description;
    }
}
