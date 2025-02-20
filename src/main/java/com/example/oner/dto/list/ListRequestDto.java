package com.example.oner.dto.list;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ListRequestDto {

    @NotNull(message = "보드 ID는 필수 입니다.")
    private Long boardId;

    @NotBlank(message = "리스트 제목은 필수 입니다.")
    private String listTitle;

    private int positionList;


    public ListRequestDto(Long boardId, String listTitle, int positionList) {
        this.boardId = boardId;
        this.listTitle = listTitle;
        this.positionList = positionList;
    }

}

