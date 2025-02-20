package com.example.oner.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;


@Entity
@Getter
@Table(name = "attachment")
public class Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id")
    private Card card;

    private String fileName;
    private String fileType;
    private long fileSize;
    private String fileUrl;
    private LocalDateTime createdAt;

    public Attachment() {
        this.createdAt = LocalDateTime.now();
    }

    public Attachment(Card card, String fileName, String fileType, long fileSize, String fileUrl) {
        this.card = card;
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.fileUrl = fileUrl;
        this.createdAt = LocalDateTime.now();
    }

}
