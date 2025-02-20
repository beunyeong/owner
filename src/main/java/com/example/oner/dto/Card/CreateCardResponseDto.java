package com.example.oner.dto.Card;

import lombok.Getter;

@Getter
public class CreateCardResponseDto {

    private final String message;
    private final Long id;
    private final Long listId;
    private final Long memberId;
    private final String title;

    public CreateCardResponseDto(String message, Long id, Long listId, Long memberId, String title) {
        this.message = message;
        this.id = id;
        this.listId = listId;
        this.memberId = memberId;
        this.title = title;
    }

}
