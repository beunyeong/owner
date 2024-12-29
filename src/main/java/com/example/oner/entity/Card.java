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
    private ListEntity list;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String cardTitle;

    private String description;

    private LocalDateTime dueDate;

    private long views;

    private boolean modified;

    private Long beforeListId;

    private Long beforeMemberId;


    public Card() {
        this.views = 0;
    }

    public Card(ListEntity list, Member member, String cardTitle, String description, LocalDateTime dueDate) {
        this.list = list;
        this.member = member;
        this.cardTitle = cardTitle;
        this.description = description;
        this.dueDate = dueDate;
        this.views = 0;
        this.modified = false;
        this.beforeMemberId = null;
        this.beforeListId = null;
    }

    public void updateCard(ListEntity newList, Member newMember, String newTitle, String newDescription, LocalDateTime newDueDate) {

        if (!this.list.getId().equals(newList.getId())) {
            this.beforeListId = this.list.getId();
            this.list = newList;
        }
        if (!this.member.getId().equals(newMember.getId())) {
            this.beforeMemberId = this.member.getId();
            this.member = newMember;
        }
        this.cardTitle = newTitle;
        this.description = newDescription;
        this.dueDate = newDueDate;
        this.modified = true;
    }


    public void plusViews(long l) {
        this.views += l;
    }
}
