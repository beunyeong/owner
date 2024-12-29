package com.example.oner.dto.comment;

import com.example.oner.entity.Comment;
import com.example.oner.enums.UserStatus;

public class CommentResponseDto {

    private final Long id;

    private final Long memberId;

    private final Long cardId;

    private final String detail;


    public CommentResponseDto(Long id, Long memberId, Long cardId, String detail) {
        this.id = id;
        this.memberId = memberId;
        this.cardId = cardId;
        this.detail = detail;
    }

    public CommentResponseDto(Comment comment){
        this.id = comment.getId();
        this.memberId = comment.getMember().getId();
        this.cardId = comment.getCard().getId();
        this.detail = comment.getDetail();
    }
}
