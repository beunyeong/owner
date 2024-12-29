package com.example.oner.controller;


import com.example.oner.dto.Member.*;
import com.example.oner.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/{userId}/invitations")
    public ResponseEntity<MemberCreateResponseDto> createMember (
            @PathVariable Long userId,
            @RequestBody MemberCreateRequestDto memberCreateRequestDto) {

        return memberService.createMember(userId, memberCreateRequestDto);
    }

    @GetMapping
    public ResponseEntity<MemberResponseDto> getAllUsers(
            @RequestBody MemberRequestDto memberRequestDto){

        return memberService.getAllUsers(memberRequestDto);
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<MemberSelectResponseDto> getUser(
            @PathVariable Long memberId,
            @RequestParam Long workspaceId){
        return memberService.getUser(memberId, workspaceId);
    }

    @PatchMapping("invite")
    public ResponseEntity<MemberInviteResponseDto> resultInvite(
            @RequestBody MemberInviteRequestDto requestDto) {
        return memberService.resultInvite(requestDto);
    }

    @DeleteMapping("/workspace/{workspaceId}")
    public ResponseEntity<Map<String, String>> leaveWorkspace(
            @PathVariable Long workspaceId
    ){
        return memberService.leaveWorkspace(workspaceId);
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<Map<String, String>> fireMember(
            @PathVariable Long memberId
    ) {
        return memberService.fireMember(memberId);
    }

    @PatchMapping("/{memberId}")
    public ResponseEntity<MemberRoleUpdateResponseDto> updateMemberRole(
            @PathVariable Long memberId,
            @RequestBody MemberRoleUpdateRequestDto memberRoleUpdateRequestDto
    ){
        return memberService.updateMemberRole(memberId, memberRoleUpdateRequestDto);
    }


}
