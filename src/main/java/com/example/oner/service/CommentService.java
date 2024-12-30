package com.example.oner.service;


import com.example.oner.config.auth.UserDetailsImpl;
import com.example.oner.dto.comment.CommentRequestDto;
import com.example.oner.dto.comment.CommentResponseDto;
import com.example.oner.dto.comment.CommentUpdateRequestDto;
import com.example.oner.entity.Card;
import com.example.oner.entity.Comment;
import com.example.oner.entity.Member;
import com.example.oner.entity.User;
import com.example.oner.enums.MemberRole;
import com.example.oner.error.errorcode.ErrorCode;
import com.example.oner.error.exception.CustomException;
import com.example.oner.repository.CardRepository;
import com.example.oner.repository.CommentRepository;
import com.example.oner.repository.MemberRepository;
import com.slack.api.methods.SlackApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "Security::AccountService")
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final CardRepository cardRepository;
    private final SlackService slackService;

    // 댓글 생성
    public CommentResponseDto createComment(CommentRequestDto requestDto , User loginUser){

        Member findMember = memberRepository.findByUserIdAndWorkspaceId(
                        loginUser.getId() , requestDto.getWorkspaceId())
                .orElseThrow(()-> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        // member 권한 확인
        if (findMember.getRole() == MemberRole.READ){
            throw new CustomException(ErrorCode.UNAUTHORIZED_ACCESS);
        }

        Card findCard = cardRepository.findById(requestDto.getCardId())
                .orElseThrow(()-> new CustomException(ErrorCode.CARD_NOT_FOUND));

        Comment comment = new Comment(findCard , findMember , requestDto.getDetail());
        commentRepository.save(comment);

        String message = loginUser.getName()+"님께서 댓글을 작성했습니다.✒️";
        sendMessageFromSlack(message);

        return new CommentResponseDto(comment);
    }

    // 댓글 조회
    public CommentResponseDto getComment(Long commentId){
        Comment findComment = commentRepository.findByIdOrElseThrow(commentId);
        return new CommentResponseDto(findComment);
    }

    //댓글 수정
    @Transactional
    public CommentResponseDto modifyComment(CommentUpdateRequestDto requestDto , User loginUser){

        Comment findComment = commentRepository.findByIdOrElseThrow(requestDto.getCommentId());

        // 해당 댓글의 작성자인지 확인
        if (!Objects.equals(loginUser.getId(), findComment.getMember().getUser().getId())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_USER);
        }

        findComment.updateDetail(requestDto.getDetail());
        return new CommentResponseDto(findComment);
    }


    public void deleteComment(Long commentId , User loginUser){

        Comment findComment = commentRepository.findByIdOrElseThrow(commentId);

        // 해당 댓글의 작성자인지 확인
        if (!Objects.equals(loginUser.getId(), findComment.getMember().getUser().getId())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_USER);
        }
        commentRepository.delete(findComment);
    }

    private void sendMessageFromSlack(String message) {
        try {
            slackService.sendMessage(message);
            log.info("Message sent successfully!");
        } catch (IOException | SlackApiException e) {
            throw new CustomException(ErrorCode.MESSAGE_SENDING_ERROR);
        }
    }

}
