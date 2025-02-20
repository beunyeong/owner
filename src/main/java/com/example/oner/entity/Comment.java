package com.example.oner.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "comment")
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id")
    private Card card;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String detail;

    public Comment() {}

    public Comment(Card card , Member member ,String detail){
        this.card = card;
        this.member = member;
        this.detail = detail;
    }

    public void updateDetail(String detail){
        this.detail = detail;
    }

}
