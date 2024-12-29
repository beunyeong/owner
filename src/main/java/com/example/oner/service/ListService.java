package com.example.oner.service;

import com.example.oner.config.auth.UserDetailsImpl;
import com.example.oner.dto.list.ListRequestDto;
import com.example.oner.dto.list.ListResponseDto;
import com.example.oner.entity.Board;
import com.example.oner.entity.ListEntity;
import com.example.oner.entity.Member;
import com.example.oner.enums.MemberRole;
import com.example.oner.error.errorcode.ErrorCode;
import com.example.oner.error.exception.CustomException;
import com.example.oner.repository.BoardRepository;
import com.example.oner.repository.ListRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ListService {

    private final ListRepository listRepository;
    private final BoardRepository boardRepository;

    public ListService(ListRepository listRepository,
                       BoardRepository boardRepository) {
        this.listRepository = listRepository;
        this.boardRepository = boardRepository;
    }

    // 1. 리스트 생성
    @Transactional
    public ListResponseDto createList(ListRequestDto requestDto) {
        // 인증 객체를 이용해 로그인한 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Member member = userDetails.getMember();


        // 보드 조회
        Board board = boardRepository.findById(requestDto.getBoardId())
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));

        // 권한 확인: 사용자에게 BOARD 권한이 있는지 검증
        if(!member.hasPermission(MemberRole.BOARD)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_ACCESS);
        }

        // position_list 중복 검증
        boolean ispositionTaken = listRepository.existsByBoardIdAndPositionList(board.getId(), requestDto.getPositionList());
        if(ispositionTaken) {
            throw new CustomException(ErrorCode.DUPLICATE_POSITION_LIST);
        }

        // 리스트 생성
        ListEntity list = new ListEntity(board, requestDto.getListTitle(), requestDto.getPositionList());
        listRepository.save(list);

        return new ListResponseDto(list);

    }

    // 2. 리스트 수정
    @Transactional
    public ListResponseDto updateList(Long listId, ListRequestDto requestDto) {
        // 인증 객체를 이용해 로그인한 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Member member = userDetails.getMember();

        // 리스트 조회
        ListEntity list = listRepository.findById(listId)
                .orElseThrow(() -> new CustomException(ErrorCode.LIST_NOT_FOUND));

        // 권한 확인: 사용자에게 BOARD 권한이 있는지 검증
        if(!member.hasPermission(MemberRole.BOARD)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_ACCESS);
        }

        // 위치 업데이트
        int currentPosition = list.getPositionList();
        int newPosition = requestDto.getPositionList();
        Board board = list.getBoard();

        // 위치 변경에 따른 기존 리스트들의 위치 조정
        if (currentPosition < newPosition) {
            // 아래로 이동: currentPosition+1 ~ newPosition 범위의 리스트를 -1 감소
            listRepository.updatePositionDecrement(board.getId(), currentPosition + 1, newPosition);
        } else if (currentPosition > newPosition) {
            // 위로 이동: newPosition ~ currentPosition-1 범위의 리스트를 +1 증가
            listRepository.updatePositionIncrement(board.getId(), newPosition, currentPosition - 1);
        }

        // 리스트 정보 업데이트
        list.setListTitle(requestDto.getListTitle());
        list.setPositionList(newPosition);
        listRepository.save(list);

        return new ListResponseDto(list);
    }

    // 3. 리스트 삭제
    @Transactional
    public void deleteList(Long listId) {
        // 인증 객체를 이용해 로그인한 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Member member = userDetails.getMember();

        // 리스트 조회
        ListEntity list = listRepository.findById(listId)
                .orElseThrow(() -> new CustomException(ErrorCode.LIST_NOT_FOUND));

        // 권한 확인: 사용자에게 BOARD 권한이 있는지 검증
        if(!member.hasPermission(MemberRole.BOARD)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_ACCESS);
        }

        // 리스트 삭제
        listRepository.delete(list);
    }

}

