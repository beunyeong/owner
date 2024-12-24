package com.example.oner.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Lists {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    private String listTitle;

    private int positionList;

    public Lists() {}

    public Lists(Board board, String listTitle, int positionList) {
        this.board = board;
        this.listTitle = listTitle;
        this.positionList = positionList;
    }





}
