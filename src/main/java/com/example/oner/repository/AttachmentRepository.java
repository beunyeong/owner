package com.example.oner.repository;

import com.example.oner.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

    Optional<Object> findByIdAndCardId(Long attachmentId, Long cardId);

    List<Attachment> findAllByCardId(Long cardId);

}
