package com.example.oner.dto.Card;

import com.example.oner.entity.Card;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CardDetailResponseDto {
    private final Long id;
    private final Long listId;
    private final Long memberId;
    private final String title;
    private final String description;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final LocalDateTime dueDate;
    private final long views;
    private final boolean modified;
    private final Long beforeId;

    public CardDetailResponseDto(Card card) {
        this.id = card.getId();
        this.listId = card.getList().getId();
        this.memberId = card.getMember().getId();
        this.title = card.getCardTitle();
        this.description = card.getDescription();
        this.createdAt = card.getCreatedAt();
        this.updatedAt = card.getUpdatedAt();
        this.dueDate = card.getDueDate();
        this.views = card.getViews();
        this.modified = card.isModified();
        this.beforeId = card.getBeforeListId();

    }
}
