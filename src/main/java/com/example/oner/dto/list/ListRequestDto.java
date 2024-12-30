package com.example.oner.dto.list;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ListRequestDto {

    @NotNull(message = "워크스페이스 ID는 필수 입니다.")
    private Long workspaceId;

    @NotNull(message = "보드 ID는 필수 입니다.")
    private Long boardId;

    @NotBlank(message = "리스트 제목은 필수 입니다.")
    private String listTitle;

    private int positionList;


    public ListRequestDto(Long workspaceId, Long boardId, String listTitle, int positionList) {
        this.workspaceId = workspaceId;
        this.boardId = boardId;
        this.listTitle = listTitle;
        this.positionList = positionList;
    }

}

