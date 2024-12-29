package com.example.oner.dto.Card;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CardPatchResponseDto {
    private final Long listId;
    private final Long memberId;
    private final String title;
    private final String description;
    private final LocalDateTime dueDate;
    private final long views;
    private final boolean modified;
    private final Long beforeListId;
    private final Long beforeMemberId;

    public CardPatchResponseDto(Long listId, Long memberId, String title, String description, LocalDateTime dueDate, long views, boolean modified, Long beforeListId, Long beforeMemberId) {
        this.listId = listId;
        this.memberId = memberId;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.views = views;
        this.modified = modified;
        this.beforeListId = beforeListId;
        this.beforeMemberId = beforeMemberId;
    }

}
