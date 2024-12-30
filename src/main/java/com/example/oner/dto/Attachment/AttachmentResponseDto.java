package com.example.oner.dto.Attachment;

import com.example.oner.entity.Attachment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AttachmentResponseDto {
    private final Long id;
    private final String fileName;
    private final String fileType;
    private final long fileSize;
    private final String fileUrl;
    private final LocalDateTime createdAt;

    public AttachmentResponseDto(Long id, String fileName, String fileType, long fileSize, String fileUrl, LocalDateTime createdAt) {
        this.id = id;
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.fileUrl = fileUrl;
        this.createdAt = createdAt;
    }

    public AttachmentResponseDto(Attachment attachment) {
        this.id = attachment.getId();
        this.fileName = attachment.getFileName();
        this.fileType = attachment.getFileType();
        this.fileSize = attachment.getFileSize();
        this.fileUrl = attachment.getFileUrl();
        this.createdAt = attachment.getCreatedAt();
    }
}
