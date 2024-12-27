package com.example.oner.controller;

import com.example.oner.dto.list.ListRequestDto;
import com.example.oner.dto.list.ListResponseDto;
import com.example.oner.service.ListService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lists")
public class ListController {

    private final ListService listService;

    public ListController(ListService listService) {
        this.listService = listService;
    }

    // 1. 리스트 생성
    @PostMapping
    public ResponseEntity<ListResponseDto> createList(@RequestBody ListRequestDto requestDto) {
        ListResponseDto listResponseDto = listService.createList(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(listResponseDto);
    }

    // 2. 리스트 수정
    @PatchMapping("/{listId}")
    public ResponseEntity<ListResponseDto> updateList(@PathVariable Long listId, @RequestBody ListRequestDto listRequestDto) {
        ListResponseDto listResponseDto = listService.updateList(listId, listRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(listResponseDto);
    }

    // 3. 리스트 삭제
    @DeleteMapping("/{listId}")
    public ResponseEntity<String> deleteList(
            @PathVariable Long listId) {
        listService.deleteList(listId);
        return ResponseEntity.ok("리스트가 성공적으로 삭제되었습니다.");
    }

}

