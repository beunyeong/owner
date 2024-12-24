package com.example.oner.entity;


import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Members member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id")
    private Workspace workspace;

    private String boardTitle;

    private String backgroundColor;

    private String backgroundImageUrl;

    public Board(){}

    public Board(Members member, Workspace workspace, String boardTitle, String backgroundColor, String backgroundImageUrl) {
        this.member = member;
        this.workspace = workspace;
        this.boardTitle = boardTitle;
        this.backgroundColor = backgroundColor;
        this.backgroundImageUrl = backgroundImageUrl;
    }


}
