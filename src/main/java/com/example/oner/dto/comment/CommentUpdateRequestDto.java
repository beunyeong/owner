package com.example.oner.dto.comment;

import jakarta.persistence.Column;
import lombok.Getter;

@Getter
public class CommentUpdateRequestDto {

    @Column(nullable = false)
    private final Long commentId;

    @Column(nullable = false)
    private final String detail;

    public CommentUpdateRequestDto(Long commentId, String detail) {
        this.commentId = commentId;
        this.detail = detail;
    }
}
