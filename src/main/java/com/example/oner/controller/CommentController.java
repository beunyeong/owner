package com.example.oner.controller;


import com.example.oner.config.auth.UserDetailsImpl;
import com.example.oner.dto.comment.CommentRequestDto;
import com.example.oner.dto.comment.CommentResponseDto;
import com.example.oner.dto.comment.CommentUpdateRequestDto;
import com.example.oner.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(@RequestBody CommentRequestDto requestDto,
                                                            @AuthenticationPrincipal UserDetailsImpl userDetails){
        return new  ResponseEntity<>(commentService.createComment(requestDto , userDetails.getUser() ) , HttpStatus.CREATED);
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> getComment(@PathVariable Long commentId){
        return new ResponseEntity<>(commentService.getComment(commentId),HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<CommentResponseDto> modifyComment(@RequestBody CommentUpdateRequestDto requestDto,
                                                            @AuthenticationPrincipal UserDetailsImpl userDetails){
        return new ResponseEntity<>(commentService.modifyComment(requestDto , userDetails.getUser()),HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId,
                                                @AuthenticationPrincipal UserDetailsImpl userDetails){
        commentService.deleteComment(commentId , userDetails.getUser());
        return  ResponseEntity.ok("삭제되었습니다.");
    }
}
