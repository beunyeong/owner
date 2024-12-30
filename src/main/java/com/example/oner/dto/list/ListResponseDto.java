package com.example.oner.dto.list;

import com.example.oner.entity.ListEntity;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class ListResponseDto {

    private Long id;

    private Long boardId;

    private String listTitle;

    private int positionList;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


    public ListResponseDto(ListEntity list) {
        this.id = list.getId();
        this.boardId = list.getBoard().getId();
        this.listTitle = list.getListTitle();
        this.positionList = list.getPositionList();
        this.createdAt = list.getCreatedAt();
        this.updatedAt = list.getUpdatedAt();
    }

}

