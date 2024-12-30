package com.example.oner.controller;


import com.example.oner.dto.comment.CommentRequestDto;
import com.example.oner.dto.comment.CommentResponseDto;
import com.example.oner.dto.comment.CommentUpdateRequestDto;
import com.example.oner.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(@RequestBody CommentRequestDto requestDto){

        return new  ResponseEntity<>(commentService.createComment(requestDto) , HttpStatus.CREATED);
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> getComment(@PathVariable Long commentId){
        return new ResponseEntity<>(commentService.getComment(commentId),HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<CommentResponseDto> modifyComment(@RequestBody CommentUpdateRequestDto requestDto){
        return new ResponseEntity<>(commentService.modifyComment(requestDto),HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId){
        commentService.deleteComment(commentId);
        return new ResponseEntity<>("삭제되었습니다" , HttpStatus.OK);
    }
}
