package com.example.oner.dto.comment;

import jakarta.persistence.Column;
import lombok.Getter;

@Getter
public class CommentRequestDto {

    @Column(nullable = false)
    private final Long workspaceId;

    @Column(nullable = false)
    private final Long cardId;

    @Column(nullable = false)
    private final String detail;

    public CommentRequestDto(Long workspaceId, Long cardId, String detail) {
        this.workspaceId = workspaceId;
        this.cardId = cardId;
        this.detail = detail;
    }
}
