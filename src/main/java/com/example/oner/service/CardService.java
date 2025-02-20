package com.example.oner.service;


import com.example.oner.config.auth.UserDetailsImpl;
import com.example.oner.dto.Card.*;
import com.example.oner.entity.*;
import com.example.oner.enums.MemberRole;
import com.example.oner.error.exception.MemberNotAuthorizedException;
import com.example.oner.repository.CardRepository;
import com.example.oner.repository.ListRepository;
import com.example.oner.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final ListRepository listRepository;
    private final MemberRepository memberRepository;

    public ResponseEntity<CreateCardResponseDto> createCard(CreateCardRequestDto createCardRequestDto) {
        //현재 로그인한 유저의 정보
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();

        ListEntity list = listRepository.findById(createCardRequestDto.getListId())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 리스트 ID입니다."));

        Member member = memberRepository.findById(createCardRequestDto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 멤버 ID입니다."));

        // 카드 생성
        Card card = new Card(
                list,
                member,
                createCardRequestDto.getTitle(),
                createCardRequestDto.getDescription(),
                createCardRequestDto.getDueDate()
        );

        cardRepository.save(card);

        CreateCardResponseDto createCardResponseDto = new CreateCardResponseDto(
                "카드 생성 완료",
                card.getId(),
                list.getId(),
                member.getId(),
                card.getCardTitle()
        );

        return ResponseEntity.status(201).body(createCardResponseDto);


    }

    public ResponseEntity<Map<String, String>> deleteCard( Long cardId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();

        Optional<Card> cardOptional = cardRepository.findById(cardId);
        if (cardOptional.isEmpty()) {
            throw new EntityNotFoundException();
        }
        Card card = cardOptional.get();

        Workspace workspace = card.getList().getBoard().getWorkspace();

        Member member = (Member) memberRepository.findByUserAndWorkspace(user, workspace)
                .orElseThrow(MemberNotAuthorizedException::new);

        if (member.getRole() == MemberRole.READ) {
            throw new MemberNotAuthorizedException();
        }
        cardRepository.delete(card);

        Map<String, String> response = new HashMap<>();
        response.put("message", "카드가 성공적으로 삭제되었습니다.");
        return ResponseEntity.ok(response);

    }

    public ResponseEntity<CardsResponseDto> getAllCards(CardRequestDto cardRequestDto){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();

        Long workspaceId = cardRequestDto.getWorkspaceId();

        boolean isMember = memberRepository.existsByUserAndWorkspaceId(user, workspaceId);
        if (!isMember) {
            throw new EntityNotFoundException("워크스페이스에 속한 멤버가 아닙니다.");
        }

        List<Card> cards = cardRepository.findByWorkspaceId(workspaceId);

        List<CardResponseDto> cardResponseDtos = cards.stream()
                .map(card -> new CardResponseDto(
                        card.getId(),
                        card.getList().getId(),
                        card.getMember().getId(),
                        card.getCardTitle(),
                        card.getDescription()
                ))
                .toList();

        CardsResponseDto responseDto = new CardsResponseDto(workspaceId, cardResponseDtos);

        return ResponseEntity.status(200).body(responseDto);


    }

    public ResponseEntity<CardPatchResponseDto> updateCard(Long cardId, UpdateCardRequestDto updateCardRequestDto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();

        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new EntityNotFoundException("카드를 찾을 수 없습니다."));

        Workspace workspace = card.getList().getBoard().getWorkspace();

        Member member = (Member) memberRepository.findByUserAndWorkspace(user, workspace)
                .orElseThrow(MemberNotAuthorizedException::new);

        if (member.getRole() == MemberRole.READ) {
            throw new MemberNotAuthorizedException();
        }

        ListEntity newList = listRepository.findById(updateCardRequestDto.getListId())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 리스트 ID입니다."));
        Member newMember = memberRepository.findById(updateCardRequestDto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 멤버 ID입니다."));

        card.updateCard(newList, newMember, updateCardRequestDto.getTitle(), updateCardRequestDto.getDescription(), updateCardRequestDto.getDueDate());

        cardRepository.save(card);

        CardPatchResponseDto responseDto = new CardPatchResponseDto(
                card.getList().getId(),
                card.getMember().getId(),
                card.getCardTitle(),
                card.getDescription(),
                card.getDueDate(),
                card.getViews(),
                card.isModified(),
                card.getBeforeListId(),
                card.getBeforeMemberId()
        );

        return ResponseEntity.status(200).body(responseDto);
    }

    public ResponseEntity<CardDetailResponseDto> getCardDetails(Long cardId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();

        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new EntityNotFoundException("카드를 찾을 수 없습니다."));

        Workspace workspace = card.getList().getBoard().getWorkspace();
        boolean isMember = memberRepository.existsByUserAndWorkspaceId(user, workspace.getId());
        if (!isMember) {
            throw new MemberNotAuthorizedException();
        }

        card.plusViews(card.getViews() + 1);
        cardRepository.save(card);

        CardDetailResponseDto responseDto = new CardDetailResponseDto(card);
        return ResponseEntity.status(200).body(responseDto);
    }

}
