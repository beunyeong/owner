package com.example.oner.service;


import com.example.oner.config.auth.UserDetailsImpl;
import com.example.oner.dto.Member.*;
import com.example.oner.dto.User.LoginResponseDto;
import com.example.oner.entity.Member;
import com.example.oner.entity.User;
import com.example.oner.entity.Workspace;
import com.example.oner.enums.MemberRole;
import com.example.oner.enums.MemberWait;
import com.example.oner.error.exception.BadRequestException;
import com.example.oner.error.exception.MemberNotAuthorizedException;
import com.example.oner.repository.MemberRepository;
import com.example.oner.repository.UserRepository;
import com.example.oner.repository.WorkspaceRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final UserRepository userRepository;
    private final WorkspaceRepository workspaceRepository;

    //받아온 userid는 멤버에 초대될 대상의 id값
    public ResponseEntity<MemberCreateResponseDto> createMember( Long userId, MemberCreateRequestDto memberCreateRequestDto   ) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();

        //타겟 유저의 유무를 검사
        User targetUser = userRepository.findByIdOrElseThrow(userId);  //Exception 나중에 확인하기

        MemberRole role = memberCreateRequestDto.getRole();  // workspace/board/read
        Long workspaceId = memberCreateRequestDto.getWorkspaceId();

        Workspace workspace = workspaceRepository.findById(workspaceId).orElseThrow(() -> new EntityNotFoundException("해당하는 워크스페이스가 없습니다."));

        //로그인한 유저의 해당 워크스페이스 멤버정보
        Member foundMember = memberRepository.findByUserIdAndWorkspaceId(user.getId(),  workspaceId).orElseThrow(() -> new EntityNotFoundException("해당 멤버를 찾을 수 없습니다"));

        if( !MemberRole.WORKSPACE.equals(foundMember.getRole())){
            throw new MemberNotAuthorizedException(); //멤버초대권한 없는경우
        }

        Member newMember = new Member( targetUser, workspace, role);

        Member savedMember = memberRepository.save(newMember);

        MemberCreateResponseDto memberCreateResponseDto = new MemberCreateResponseDto( userId, workspaceId, savedMember.getRole() );

        return ResponseEntity.ok(memberCreateResponseDto);
    }

    public ResponseEntity<MemberResponseDto> getAllUsers( MemberRequestDto memberRequestDto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();

        Long workspaceId = memberRequestDto.getWorkspaceId();

        List<Workspace> userWorkspace = workspaceRepository.findWorkspacesByUserId(user.getId());

        // 로그인된 유저가 해당 워크스페이스의 일원인지 확인하는 과정
        boolean isWorkspaceMember = false;
        for(Workspace workspace : userWorkspace){
            if(workspace.getId().equals(workspaceId)){
                isWorkspaceMember = true;
                break;
            }
        }
        if (!isWorkspaceMember) {
            throw new MemberNotAuthorizedException();
        }
        List<Member> workspaceMembers = workspaceRepository.getMembersById(workspaceId);
        List<MemberDetailDto> memberDetailDtos = new ArrayList<>();

        for (Member member : workspaceMembers) {
            // Member 객체에서 User 객체를 가져옴
            User workspaceUser = member.getUser();

            // 멤버의 이름, 사용자 ID, 역할 정보를 가져와서 MemberDetailDto 객체 생성
            MemberDetailDto memberDetailDto = new MemberDetailDto(
                    workspaceUser.getName(),         // User의 이름
                    workspaceUser.getId(),           // User의 ID
                    member.getRole()        // Member의 역할
            );
            // 생성된 객체를 리스트에 추가
            memberDetailDtos.add(memberDetailDto);
        }
        MemberResponseDto memberResponseDto = new MemberResponseDto(workspaceId, memberDetailDtos);

        return ResponseEntity.ok(memberResponseDto);
        }

    public ResponseEntity<MemberSelectResponseDto> getUser( Long memberId, Long workspaceId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();

        boolean isUserWorkspaceMember = workspaceRepository.existsByIdAndUserId(workspaceId, user.getId());
        if (!isUserWorkspaceMember) {
            throw new MemberNotAuthorizedException();
        }

        // memberId의 멤버가 존재하는지 검사
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("해당 멤버를 찾을 수 없습니다."));

        // memberId의 멤버가 워크스페이스Id에 해당하는 워크스페이스에 존재하는지 검사
        if (!member.getWorkspace().getId().equals(workspaceId)) {
            throw new EntityNotFoundException("해당 워크스페이스에서 멤버를 찾을 수 없습니다.");
        }


        MemberSelectResponseDto memberSelectResponseDto = new MemberSelectResponseDto(member);

        return ResponseEntity.ok(memberSelectResponseDto);
    }


    public ResponseEntity<MemberInviteResponseDto> resultInvite( MemberInviteRequestDto memberInviteRequestDto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();

        Member member = (Member) memberRepository.findByUserIdAndWorkspaceIdAndWait(
                        user.getId(), memberInviteRequestDto.getWorkspaceId(), MemberWait.WAIT)
                .orElseThrow(() -> new EntityNotFoundException("초대 대기 상태인 요청을 찾을 수 없습니다."));

        member.setWait(memberInviteRequestDto.getWait());

        memberRepository.save(member);

        MemberInviteResponseDto responseDto = new MemberInviteResponseDto(
                member.getId(),
                member.getWorkspace().getId(),
                member.getRole(),
                member.getWait()
        );

        return ResponseEntity.ok(responseDto);

    }

    public ResponseEntity<Map<String, String>> leaveWorkspace(Long workspaceId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();

        Member member = memberRepository.findByUserIdAndWorkspaceId(user.getId(), workspaceId)
                .orElseThrow(() -> new EntityNotFoundException("해당 워크스페이스에서 멤버를 찾을 수 없습니다."));

        memberRepository.delete(member);

        Map<String, String> response = new HashMap<>();
        response.put("message", "멤버 탈퇴 완료");

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<Map<String, String>> fireMember(Long memberId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("멤버를 찾을 수 없습니다."));

        boolean isAdmin = memberRepository.existsByUserIdAndWorkspaceIdAndRole(
                user.getId(), member.getWorkspace().getId(), MemberRole.WORKSPACE);

        if (!isAdmin) {
            throw new MemberNotAuthorizedException();
        }
        memberRepository.delete(member);

        Map<String, String> response = new HashMap<>();
        response.put("message", "멤버 추방 완료");

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<MemberRoleUpdateResponseDto> updateMemberRole(Long memberId, MemberRoleUpdateRequestDto updateMemberRoleRequestDto ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("멤버를 찾을 수 없습니다."));

        if (!member.getWorkspace().getId().equals(updateMemberRoleRequestDto.getWorkspaceId())) {
            throw new EntityNotFoundException("해당 워크스페이스에서 멤버를 찾을 수 없습니다.");
        }

        boolean isAdmin = memberRepository.existsByUserIdAndWorkspaceIdAndRole(
                user.getId(), updateMemberRoleRequestDto.getWorkspaceId(), MemberRole.WORKSPACE);

        if (!isAdmin) {
            throw new MemberNotAuthorizedException();
        }

        try {
            MemberRole newRole = MemberRole.valueOf(String.valueOf(updateMemberRoleRequestDto.getRole()));
            member.setRole(newRole);
            memberRepository.save(member);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException();
        }
        MemberRoleUpdateResponseDto responseDto = new MemberRoleUpdateResponseDto(
                "권한수정 완료",
                member.getId(),
                member.getWorkspace().getId(),
                member.getRole()
        );

        return ResponseEntity.ok(responseDto);
    }

}

