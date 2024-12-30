package com.example.oner.dto.Card;

import lombok.Getter;

@Getter
public class CardRequestDto {
    private final Long workspaceId;

    public CardRequestDto(Long workspaceId) {
        this.workspaceId = workspaceId;
    }
}
