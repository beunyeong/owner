package com.example.oner.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "list")
public class ListEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @Setter
    private String listTitle;

    @Setter
    private int positionList;

    @OneToMany(mappedBy = "list", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Card> cards = new ArrayList<>();     // 리스트 내부의 카드

    public ListEntity() {}


    public ListEntity(Board board, String listTitle, int positionList) {
        this.board = board;
        this.listTitle = listTitle;
        this.positionList = positionList;
    }

}

