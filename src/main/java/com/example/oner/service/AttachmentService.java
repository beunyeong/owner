package com.example.oner.service;

import com.example.oner.config.auth.UserDetailsImpl;
import com.example.oner.dto.Attachment.AttachmentResponseDto;
import com.example.oner.entity.*;
import com.example.oner.enums.MemberRole;
import com.example.oner.error.exception.MemberNotAuthorizedException;
import com.example.oner.repository.AttachmentRepository;
import com.example.oner.repository.CardRepository;
import com.example.oner.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AttachmentService {

    private final S3Service s3Service;
    private final AttachmentRepository attachmentRepository;
    private final CardRepository cardRepository;
    private final MemberRepository memberRepository;

    public AttachmentService(S3Service s3Service, AttachmentRepository attachmentRepository, CardRepository cardRepository, MemberRepository memberRepository) {
        this.s3Service = s3Service;
        this.attachmentRepository = attachmentRepository;
        this.cardRepository = cardRepository;
        this.memberRepository = memberRepository;
    }

    public ResponseEntity<AttachmentResponseDto> addAttachment(Long cardId, MultipartFile file, User user) {

        if (!isValidFileType(file.getContentType())) {
            throw new IllegalArgumentException("지원되지 않는 파일 형식입니다.");
        }
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException("파일 크기가 제한을 초과합니다.");
        }

        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new EntityNotFoundException("카드를 찾을 수 없습니다."));

        Workspace workspace = card.getList().getBoard().getWorkspace();
        Member member =  memberRepository.findByUserAndWorkspace(user, workspace)
                .orElseThrow(() -> new EntityNotFoundException("워크스페이스에 속한 멤버가 아닙니다."));

        if (member.getRole() == MemberRole.READ) {
            throw new MemberNotAuthorizedException();
        }

        try {
            String key = generateS3Key(cardId, file.getOriginalFilename());
            String fileUrl = s3Service.uploadFile(key, file.getInputStream(), file.getContentType(), file.getSize());

            Attachment attachment = new Attachment(card, file.getOriginalFilename(), file.getContentType(), file.getSize(), fileUrl);
            attachmentRepository.save(attachment);

            AttachmentResponseDto responseDto = new AttachmentResponseDto(attachment);

            return ResponseEntity.status(200).body(responseDto);
        } catch (IOException e) {
            throw new RuntimeException("파일 업로드 중 오류가 발생했습니다.", e);
        }

    }

    public ResponseEntity<Map<String, String>> deleteAttachment(Long cardId, Long attachmentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();

        Attachment attachment = (Attachment) attachmentRepository.findByIdAndCardId(attachmentId, cardId)
                .orElseThrow(() -> new EntityNotFoundException("첨부파일을 찾을 수 없습니다."));

        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new EntityNotFoundException("카드를 찾을 수 없습니다."));

        Workspace workspace = card.getList().getBoard().getWorkspace();
        Member member = (Member) memberRepository.findByUserAndWorkspace(user, workspace)
                .orElseThrow(() -> new EntityNotFoundException("워크스페이스에 속한 멤버가 아닙니다."));

        if (member.getRole() == MemberRole.READ) {
            throw new MemberNotAuthorizedException();
        }

        s3Service.deleteFile(attachment.getFileUrl());

        attachmentRepository.delete(attachment);

        Map<String, String> response = new HashMap<>();
        response.put("message", "삭제되었습니다.");
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<AttachmentResponseDto>  getAttachment(Long cardId, Long attachmentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();

        Attachment attachment = (Attachment) attachmentRepository.findByIdAndCardId(attachmentId, cardId)
                .orElseThrow(() -> new EntityNotFoundException("첨부파일을 찾을 수 없습니다."));

        Workspace workspace = attachment.getCard().getList().getBoard().getWorkspace();
        Optional<Member> memberOpt = memberRepository.findByUserAndWorkspace(user, workspace);

        if (memberOpt.isEmpty()) {
            throw new EntityNotFoundException("워크스페이스에 속한 멤버가 아닙니다.");
        }

        Member member =  memberOpt.get();
        if (member.getRole() == MemberRole.READ) {
            throw new MemberNotAuthorizedException();
        }

        AttachmentResponseDto responseDto = new AttachmentResponseDto(attachment);
        return ResponseEntity.status(200).body(responseDto);

    }

    public ResponseEntity<List<AttachmentResponseDto>> getAllAttachments(Long cardId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();

        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new EntityNotFoundException("카드를 찾을 수 없습니다."));

        Workspace workspace = card.getList().getBoard().getWorkspace();
        Optional<Member> memberOpt = memberRepository.findByUserAndWorkspace(user, workspace);

        if (memberOpt.isEmpty()) {
            throw new EntityNotFoundException("워크스페이스에 속한 멤버가 아닙니다.");
        }

        Member member = memberOpt.get();
        if (member.getRole() == MemberRole.READ) {
            throw new MemberNotAuthorizedException();
        }

        List<Attachment> attachments = attachmentRepository.findAllByCardId(cardId);
        List<AttachmentResponseDto> attachmentResponseDtos = attachments.stream()
                .map(AttachmentResponseDto::new)
                .toList();

        return ResponseEntity.status(200).body(attachmentResponseDtos);

    }

    private boolean isValidFileType(String contentType) {
        return contentType.equals("image/jpeg") || contentType.equals("image/png") || contentType.equals("application/pdf") || contentType.equals("text/csv");
    }

    private String generateS3Key(Long cardId, String originalFilename) {
        return "cards/" + cardId + "/" + originalFilename;
    }


}
