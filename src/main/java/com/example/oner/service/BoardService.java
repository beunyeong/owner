package com.example.oner.service;

import com.example.oner.config.auth.UserDetailsImpl;
import com.example.oner.dto.board.BoardGetResponseDto;
import com.example.oner.dto.board.BoardRequestDto;
import com.example.oner.dto.board.BoardResponseDto;
import com.example.oner.entity.Board;
import com.example.oner.entity.Member;
import com.example.oner.entity.User;
import com.example.oner.entity.Workspace;
import com.example.oner.enums.MemberRole;
import com.example.oner.error.errorcode.ErrorCode;
import com.example.oner.error.exception.CustomException;
import com.example.oner.repository.BoardRepository;
import com.example.oner.repository.WorkspaceRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final WorkspaceRepository workspaceRepository;
    private final S3Service s3Service;

    public BoardService(BoardRepository boardRepository, WorkspaceRepository workspaceRepository, S3Service s3Service) {
        this.boardRepository = boardRepository;
        this.workspaceRepository = workspaceRepository;
        this.s3Service = s3Service;
    }

    // 1. 보드 생성
    public BoardResponseDto createBoard(BoardRequestDto requestDto) {
        // 인증 객체를 이용해 로그인한 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();      // 로그인 유저 조회
        Member member = user.getMember();   // 로그인한 유저의 첫번째 멤버 조회

        // 워크스페이스 조회
        Workspace workspace = workspaceRepository.findByIdAndMembersContaining(requestDto.getWorkspaceId(), member)
                .orElseThrow(() -> new CustomException(ErrorCode.WORKSPACE_NOT_FOUND));

        // 멤버 권한 확인
        if (!member.hasPermission(MemberRole.BOARD)) {
            throw new CustomException(ErrorCode.INVALID_USER_ROLE);
        }

        // 배경 이미지 업로드 처리
        String backgroundImageUrl = null;
        if (requestDto.getBackgroundImage() != null && !requestDto.getBackgroundImage().isEmpty()) {
            backgroundImageUrl = s3Service.uploadFile(requestDto.getBackgroundImage(), "boards");
        }

        // 보드 생성 및 저장
        Board board = new Board(member, workspace, requestDto.getTitle(), requestDto.getBackgroundColor(), backgroundImageUrl);
        boardRepository.save(board);

        return new BoardResponseDto(board);
    }

    // 2. 보드 전체 조회
    public List<BoardGetResponseDto> getBoards() {
        // 인증 객체를 이용해 로그인한 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();
        Member member = user.getMember();

        // 워크스페이스 조회
        List<Workspace> workspaces = workspaceRepository.findAllByMembersContaining(member);
        if (workspaces.isEmpty()) {
            throw new CustomException(ErrorCode.WORKSPACE_NOT_FOUND);
        }

        return boardRepository.findAllByWorkspaceIn(workspaces)
                .stream()
                .map(board -> new BoardGetResponseDto(
                        board.getId(),
                        board.getBoardTitle(),
                        board.getWorkspace().getId(),
                        board.getBackgroundColor(),
                        board.getBackgroundImageUrl(),
                        board.getCreatedAt(),
                        board.getUpdatedAt()))
                .collect(Collectors.toList());
    }

    // 3. 보드 단건 조회
    public BoardResponseDto getBoardById(Long boardId) {
        // 인증 객체를 이용해 로그인한 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();
        Member member = user.getMember();

        // 보드 조회 및 권한 확인
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));

        if (!member.isMemberOf(board.getWorkspace())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_ACCESS);
        }

        return new BoardResponseDto(board);
    }

    // 4. 보드 수정
    public BoardResponseDto updateBoard(Long boardId, BoardRequestDto requestDto) {
        // 인증 객체를 이용해 로그인한 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();
        Member member = user.getMember();

        // 보드 조회 및 권한 확인
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));

        if (!member.hasPermission(MemberRole.BOARD)) {
            throw new CustomException(ErrorCode.INVALID_USER_ROLE);
        }

        board.updateBoard(requestDto.getTitle(), requestDto.getBackgroundColor());
        boardRepository.save(board);

        return new BoardResponseDto(board);
    }

    // 5. 보드 삭제
    public void deleteBoard(Long boardId) {
        // 인증 객체를 이용해 로그인한 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();
        Member member = user.getMember();

        // 보드 조회 및 권한 확인
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));

        if (!member.hasPermission(MemberRole.BOARD)) {
            throw new CustomException(ErrorCode.INVALID_USER_ROLE);
        }

        boardRepository.delete(board);
    }
}
