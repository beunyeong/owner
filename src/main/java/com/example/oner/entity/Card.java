package com.example.oner.entity;

import jakarta.persistence.*;
import lombok.Getter;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "card")
public class Card extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "list_id")
    private List lists;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String cardTitle;

    private String description;

    private LocalDateTime dueDate;

    private long views;

    private boolean modified;

    private Long beforeId;

    public Card() {
        this.views = 0;
    }

    public Card(List lists, Member member, String cardTitle, String description, LocalDateTime dueDate) {
        this.lists = lists;
        this.member = member;
        this.cardTitle = cardTitle;
        this.description = description;
        this.dueDate = dueDate;
        this.views = 0;
        this.modified = false;
        this.beforeId = null;
    }



}
