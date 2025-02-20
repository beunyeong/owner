package com.example.oner.controller;

import com.example.oner.dto.board.BoardGetResponseDto;
import com.example.oner.dto.board.BoardRequestDto;
import com.example.oner.dto.board.BoardResponseDto;
import com.example.oner.service.BoardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/boards")
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    // 1. 보드 생성
    @PostMapping
    public ResponseEntity<BoardResponseDto> createBoard(@ModelAttribute BoardRequestDto requestDto) {
        BoardResponseDto responseDto = boardService.createBoard(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // 2. 보드 전체 조회
    @GetMapping
    public ResponseEntity<List<BoardGetResponseDto>> getBoard() {
        List<BoardGetResponseDto> response = boardService.getBoards();
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    // 3. 보드 단건 조회
    @GetMapping("/{boardId}")
    public ResponseEntity<BoardResponseDto> getBoardById(@PathVariable Long boardId) {
        BoardResponseDto board = boardService.getBoardById(boardId);
        return ResponseEntity.status(HttpStatus.OK).body(board);
    }

    // 4. 보드 수정
    @PatchMapping("/{boardId}")
    public ResponseEntity<BoardResponseDto> updateBoard(@PathVariable Long boardId, @ModelAttribute BoardRequestDto requestDto) {
        BoardResponseDto responseDto = boardService.updateBoard(boardId, requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // 5. 보드 삭제
    @DeleteMapping("/{boardId}")
    public ResponseEntity<String> deleteBoard(@PathVariable Long boardId) {
        boardService.deleteBoard(boardId);
        return ResponseEntity.ok("보드가 성공적으로 삭제되었습니다.");
    }
}

