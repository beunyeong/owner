package com.example.oner.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "list")
public class List {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    private String listTitle;

    private int positionList;

    public List() {}

    public List(Board board, String listTitle, int positionList) {
        this.board = board;
        this.listTitle = listTitle;
        this.positionList = positionList;
    }

}
