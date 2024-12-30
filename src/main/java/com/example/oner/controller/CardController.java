package com.example.oner.controller;

import com.example.oner.dto.Card.*;
import com.example.oner.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cards")
public class CardController {

    private final CardService cardService;

    @PostMapping
    public ResponseEntity<CreateCardResponseDto> createCard(
            @RequestBody CreateCardRequestDto createCardRequestDto
    ) {
        return cardService.createCard(createCardRequestDto);
    }

    @DeleteMapping("{cardId}")
    public ResponseEntity<Map<String, String>> deleteCard(
            @PathVariable Long cardId
    ) {
        return cardService.deleteCard(cardId);
    }

    @GetMapping
    public ResponseEntity<CardsResponseDto> getAllCards(
            @RequestBody CardRequestDto cardRequestDto) {
        return cardService.getAllCards(cardRequestDto);
    }

    @PatchMapping("{cardId}")
    public ResponseEntity<CardPatchResponseDto> updateCard(
            @PathVariable Long cardId,
            @RequestBody UpdateCardRequestDto updateCardRequestDto
    ) {
        return cardService.updateCard(cardId, updateCardRequestDto);
    }

    @GetMapping("{cardId}")
    public ResponseEntity<CardDetailResponseDto> getCardDetails(
            @PathVariable Long cardId
    ) {
        return cardService.getCardDetails(cardId);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<CardDetailResponseDto>> searchCards(CardSearchRequestDto request) {
        Page<CardDetailResponseDto> results = cardService.searchCards(request);
        return ResponseEntity.ok(results);
    }
}