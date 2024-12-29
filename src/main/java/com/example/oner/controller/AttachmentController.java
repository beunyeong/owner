package com.example.oner.controller;

import com.example.oner.dto.Attachment.AttachmentResponseDto;
import com.example.oner.service.AttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cards/{cardId}/attachments")
public class AttachmentController {

    private final AttachmentService attachmentService;

    @PostMapping
    public ResponseEntity<AttachmentResponseDto> addAttachment(
            @PathVariable Long cardId,
            @RequestParam("file") MultipartFile file
    ){
        return attachmentService.addAttachment(cardId, file);
    }

    @DeleteMapping("{attachmentId}")
    public ResponseEntity<Map<String, String>> deleteAttachment(
            @PathVariable Long cardId,
            @PathVariable Long attachmentId
    ){
        return attachmentService.deleteAttachment(cardId, attachmentId);
    }

    @GetMapping("/{attachmentId}")
    public ResponseEntity<AttachmentResponseDto> getAttachment(
            @PathVariable Long cardId,
            @PathVariable Long attachmentId
    ){
        return attachmentService.getAttachment(cardId, attachmentId);
    }

    @GetMapping
    public ResponseEntity<List<AttachmentResponseDto>> getAllAttachments(
            @PathVariable Long cardId
    ){
        return attachmentService.getAllAttachments(cardId);
    }

}
