package com.example.oner.service;

import com.example.oner.dto.workspace.WorkspaceResponseDto;
import com.example.oner.entity.Member;
import com.example.oner.entity.User;
import com.example.oner.entity.Workspace;
import com.example.oner.enums.MemberRole;
import com.example.oner.error.errorcode.ErrorCode;
import com.example.oner.error.exception.CustomException;
import com.example.oner.repository.MemberRepository;
import com.example.oner.repository.UserRepository;
import com.example.oner.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.oner.error.errorcode.ErrorCode.EMAIL_FORM_ERROR;
import static com.example.oner.error.errorcode.ErrorCode.INVALID_USER_ROLE;

@Service
@RequiredArgsConstructor
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;
    private final MemberRepository memberRepository;

    /**
     * 워크스페이스 생성
     * - ADMIN 권한을 가진 유저만 생성 가능
     * - 새로운 워크스페이스와 해당 워크스페이스의 기본 멤버를 생성
     *
     * @param name        워크스페이스 이름
     * @param description 워크스페이스 설명
     * @param email       요청을 보낸 유저의 이메일
     * @return 생성된 워크스페이스의 응답 DTO
     */

    @Transactional
    public WorkspaceResponseDto createWorkspace(String name, String description, String email) {
        User user = userRepository.findByEmailOrElseThrow(email);
        // ADMIN 권한 확인
        if (!user.isAdmin()) {
            throw new CustomException(INVALID_USER_ROLE);
        }

        Workspace workspace = workspaceRepository.save(
                new Workspace(user, name, description)
        );

        Member member = new Member(user, workspace, MemberRole.WORKSPACE);
        memberRepository.save(member);

        return new WorkspaceResponseDto(workspace);
    }
    /**
     * 사용자가 멤버로 속한 모든 워크스페이스 조회
     *
     * @param email 요청을 보낸 유저의 이메일
     * @return 워크스페이스 목록의 응답 DTO 리스트
     */
    public List<WorkspaceResponseDto> getAllWorkspaces(String email) {
        User user = userRepository.findByEmailOrElseThrow(email);
        // 사용자가 속한 모든 워크스페이스 멤버 조회
        List<Member> members = memberRepository.findByAllUserOrElseThrow(user);
        // 워크스페이스 응답 DTO로 변환
        return members.stream()
                .map(member -> new WorkspaceResponseDto(member.getWorkspace()))
                .collect(Collectors.toList());
    }
    /**
     * 특정 워크스페이스 조회
     * - 요청한 유저가 워크스페이스 멤버인지 확인
     *
     * @param workspaceId 조회할 워크스페이스 ID
     * @param email       요청을 보낸 유저의 이메일
     * @return 워크스페이스의 응답 DTO
     */
    public WorkspaceResponseDto getWorkspace(Long workspaceId, String email) {
        User user = userRepository.findByEmailOrElseThrow(email);

        Workspace workspace = workspaceRepository.findByIdOrElseThrow(workspaceId);

        Member member = memberRepository.findByUserAndWorkspaceOrElseThrow(user, workspace);

        return new WorkspaceResponseDto(workspace);
    }
    /**
     * 워크스페이스 정보 수정
     * - 요청한 유저가 워크스페이스 멤버인지 확인
     *
     * @param workspaceId 수정할 워크스페이스 ID
     * @param name        새로운 이름
     * @param description 새로운 설명
     * @param email       요청을 보낸 유저의 이메일
     * @return 수정된 워크스페이스의 응답 DTO
     */
    @Transactional
    public WorkspaceResponseDto updateWorkspace(Long workspaceId, String name, String description, String email) {
        User user = userRepository.findByEmailOrElseThrow(email);
        Workspace workspace = workspaceRepository.findByIdOrElseThrow(workspaceId);

        // 요청한 유저가 해당 워크스페이스 멤버인지 확인
        if (!memberRepository.existsByUserAndWorkspace(user, workspace)) {
            throw new CustomException(EMAIL_FORM_ERROR);
        }

        workspace.updateWorkspace(name, description);
        return new WorkspaceResponseDto(workspace);
    }
    /**
     * 워크스페이스 삭제
     * - 요청한 유저가 워크스페이스 멤버인지 확인
     *
     * @param workspaceId 삭제할 워크스페이스 ID
     * @param email       요청을 보낸 유저의 이메일
     */

    @Transactional
    public void deleteWorkspace(Long workspaceId, String email) {
        //유저조회
        User user = userRepository.findByEmailOrElseThrow(email);
        //워크스페이스 조회
        Workspace workspace = workspaceRepository.findByIdOrElseThrow(workspaceId);

        // 해당 워크스페이스 멤버인지 권한 확인
        if (!memberRepository.existsByUserAndWorkspace(user, workspace)) {
            throw new CustomException(EMAIL_FORM_ERROR);
        }

        workspaceRepository.delete(workspace);
    }
}