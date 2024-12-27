package com.example.oner.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "board")
public class Board extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id")
    private Workspace workspace;

    private String boardTitle;

    private String backgroundColor;

    private String backgroundImageUrl;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ListEntity> lists = new ArrayList<>();   // 보드 내부의 리스트

    public Board(){}

    public Board(Member member, Workspace workspace, String boardTitle, String backgroundColor, String backgroundImageUrl) {
        this.member = member;
        this.workspace = workspace;
        this.boardTitle = boardTitle;
        this.backgroundColor = backgroundColor;
        this.backgroundImageUrl = backgroundImageUrl;
    }

    public void updateBoard(String boardTitle, String backgroundColor) {
        if (boardTitle != null && !boardTitle.isEmpty()) {
            this.boardTitle = boardTitle;
        }
        if (backgroundColor != null && !backgroundColor.isEmpty()) {
            this.backgroundColor = backgroundColor;
        }
    }

}
