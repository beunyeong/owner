package com.example.oner.service;


import com.example.oner.config.Const;
import com.example.oner.dto.Member.MemberCreateRequestDto;
import com.example.oner.dto.Member.MemberCreateResponseDto;
import com.example.oner.dto.User.LoginResponseDto;
import com.example.oner.entity.Member;
import com.example.oner.entity.User;
import com.example.oner.entity.Workspace;
import com.example.oner.enums.MemberRole;
import com.example.oner.error.exception.MemberNotAuthorizedException;
import com.example.oner.repository.MemberRepository;
import com.example.oner.repository.UserRepository;
import com.example.oner.repository.WorkspaceRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final UserRepository userRepository;
    private final WorkspaceRepository workspaceRepository;

    //받아온 userid는 멤버에 초대될 대상의 id값
    public ResponseEntity<MemberCreateResponseDto> createMember( Long userid, HttpServletRequest request, MemberCreateRequestDto memberCreateRequestDto   ) {

        LoginResponseDto loginResponseDto = (LoginResponseDto) request.getSession().getAttribute(Const.LOGIN_USER);

        //타겟 유저의 유무를 검사
        User targetUser = userRepository.findByIdOrElseThrow(userid);  //Exception 나중에 확인하기

        MemberRole role = memberCreateRequestDto.getRole();  // workspace/board/read
        Long workspaceId = memberCreateRequestDto.getWorkspaceId();

        Workspace workspace = workspaceRepository.findById(workspaceId).orElseThrow(() -> new EntityNotFoundException("해당하는 워크스페이스가 없습니다."));

        //로그인한 유저의 해당 워크스페이스 멤버정보
        Member foundMember = memberRepository.findByUserIdAndWorkspaceId(loginResponseDto.getUserId(),  workspaceId).orElseThrow(() -> new EntityNotFoundException("해당 멤버를 찾을 수 없습니다"));

        if( !MemberRole.WORKSPACE.equals(foundMember.getRole())){
            throw new MemberNotAuthorizedException(); //멤버초대권한 없는경우
        }

        Member newMember = new Member( targetUser, workspace, role);

        Member savedMember = memberRepository.save(newMember);

        MemberCreateResponseDto memberCreateResponseDto = new MemberCreateResponseDto( userid, workspaceId, savedMember.getRole() );

        return ResponseEntity.ok(memberCreateResponseDto);
    }

}
