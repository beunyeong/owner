package com.example.oner.dto.Card;

import lombok.Getter;

import java.util.List;

@Getter
public class CardsResponseDto {

    private final Long workspaceId;
    private final List<CardResponseDto> cards;

    public CardsResponseDto(Long workspaceId, List<CardResponseDto> cards) {
        this.workspaceId = workspaceId;
        this.cards = cards;
    }
}
