package com.example.oner.controller;

import com.example.oner.dto.common.CommonResponseBody;
import com.example.oner.dto.workspace.WorkspaceRequestDto;
import com.example.oner.dto.workspace.WorkspaceResponseDto;
import com.example.oner.service.WorkspaceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/workspaces")
public class WorkspaceController {

    private final WorkspaceService workspaceService;
    /**
     * 워크스페이스 생성
     * - 요청된 워크스페이스 정보를 받아 새 워크스페이스를 생성합니다.
     *
     * @param workspaceRequestDto 생성할 워크스페이스 정보 (이름, 설명 포함)
     * @param authentication 인증 정보 (요청을 보낸 유저의 이름 사용)
     * @return 생성된 워크스페이스 정보와 HTTP 상태 코드 201(CREATED)
     */
    @PostMapping
    public ResponseEntity<CommonResponseBody<WorkspaceResponseDto>> createWorkspace(
            @Valid @RequestBody WorkspaceRequestDto workspaceRequestDto,
            Authentication authentication
    ) {
        WorkspaceResponseDto workspace = workspaceService.createWorkspace(
                workspaceRequestDto.getName(),
                workspaceRequestDto.getDescription(),
                authentication.getName()
        );
        return new ResponseEntity<>(new CommonResponseBody<>("워크스페이스를 생성했습니다", workspace), HttpStatus.CREATED);
    }
    /**
     * 워크스페이스 다건 조회
     * - 요청한 사용자가 멤버로 가입된 모든 워크스페이스를 조회합니다.
     *
     * @param authentication 인증 정보 (요청을 보낸 유저의 이름 사용)
     * @return 사용자가 속한 워크스페이스 목록과 HTTP 상태 코드 200(OK)
     */
    @GetMapping
    public ResponseEntity<CommonResponseBody<List<WorkspaceResponseDto>>> getAllWorkspaces(
            Authentication authentication
    ) {
        List<WorkspaceResponseDto> workspaces = workspaceService.getAllWorkspaces(authentication.getName());

        return new ResponseEntity<>(new CommonResponseBody<>("워크스페이스 다건 조회", workspaces), HttpStatus.OK);
    }
    /**
     * 워크스페이스 단건 조회
     * - 특정 워크스페이스의 상세 정보를 조회합니다.
     * - 요청한 사용자가 해당 워크스페이스의 멤버인지 확인합니다.
     *
     * @param workspaceId 조회할 워크스페이스 ID
     * @param authentication 인증 정보 (요청을 보낸 유저의 이름 사용)
     * @return 조회된 워크스페이스 정보와 HTTP 상태 코드 200(OK)
     */
    @GetMapping("/{workspaceId}")
    public ResponseEntity<CommonResponseBody<WorkspaceResponseDto>> getWorkspace(
            @PathVariable Long workspaceId,
            Authentication authentication
    ) {
        WorkspaceResponseDto workspace = workspaceService.getWorkspace(workspaceId, authentication.getName());

        return new ResponseEntity<>(new CommonResponseBody<>("워크스페이스 단건 조회", workspace), HttpStatus.OK);
    }
    /**
     * 워크스페이스 수정
     * - 특정 워크스페이스의 이름과 설명을 수정합니다.
     * - 요청한 사용자가 해당 워크스페이스의 멤버인지 확인합니다.
     *
     * @param workspaceId 수정할 워크스페이스 ID
     * @param workspaceRequestDto 수정할 워크스페이스 정보 (이름, 설명 포함)
     * @param authentication 인증 정보 (요청을 보낸 유저의 이름 사용)
     * @return 수정된 워크스페이스 정보와 HTTP 상태 코드 200(OK)
     */
    @PatchMapping("/{workspaceId}")
    public ResponseEntity<CommonResponseBody<WorkspaceResponseDto>> updateWorkspace(
            @PathVariable Long workspaceId,
            @Valid @RequestBody WorkspaceRequestDto workspaceRequestDto,
            Authentication authentication
    ) {
        WorkspaceResponseDto workspace = workspaceService.updateWorkspace(
                workspaceId,
                workspaceRequestDto.getName(),
                workspaceRequestDto.getDescription(),
                authentication.getName()
        );

        return new ResponseEntity<>(new CommonResponseBody<>("워크스페이스를 수정했습니다", workspace), HttpStatus.OK);
    }
    /**
     * 워크스페이스 삭제
     * - 특정 워크스페이스를 삭제합니다.
     * - 요청한 사용자가 해당 워크스페이스의 멤버인지 확인합니다.
     *
     * @param workspaceId 삭제할 워크스페이스 ID
     * @param authentication 인증 정보 (요청을 보낸 유저의 이름 사용)
     * @return HTTP 상태 코드 204(NO_CONTENT) - 응답 본문 없음
     */
    @DeleteMapping("/{workspaceId}")
    public ResponseEntity<Void> deleteWorkspace(
            @PathVariable Long workspaceId,
            Authentication authentication
    ) {
        workspaceService.deleteWorkspace(workspaceId, authentication.getName());

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}