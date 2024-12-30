package com.example.oner.controller;


import com.example.oner.config.auth.UserDetailsImpl;
import com.example.oner.dto.Member.*;
import com.example.oner.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
            @RequestBody MemberCreateRequestDto memberCreateRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return memberService.createMember(userId, memberCreateRequestDto, userDetails.getUser());
    }

    @GetMapping
    public ResponseEntity<MemberResponseDto> getAllUsers(
            @RequestBody MemberRequestDto memberRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails){

        return memberService.getAllUsers(memberRequestDto, userDetails.getUser());
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<MemberSelectResponseDto> getUser(
            @PathVariable Long memberId,
            @RequestParam Long workspaceId,
            @AuthenticationPrincipal UserDetailsImpl userDetails){
        return memberService.getUser(memberId, workspaceId, userDetails.getUser());
    }

    @PatchMapping("invite")
    public ResponseEntity<MemberInviteResponseDto> resultInvite(
            @RequestBody MemberInviteRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return memberService.resultInvite(requestDto, userDetails.getUser());
    }

    @DeleteMapping("/workspace/{workspaceId}")
    public ResponseEntity<Map<String, String>> leaveWorkspace(
            @PathVariable Long workspaceId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        return memberService.leaveWorkspace(workspaceId, userDetails.getUser());
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<Map<String, String>> fireMember(
            @PathVariable Long memberId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return memberService.fireMember(memberId, userDetails.getUser());
    }

    @PatchMapping("/{memberId}")
    public ResponseEntity<MemberRoleUpdateResponseDto> updateMemberRole(
            @PathVariable Long memberId,
            @RequestBody MemberRoleUpdateRequestDto memberRoleUpdateRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        return memberService.updateMemberRole(memberId, memberRoleUpdateRequestDto, userDetails.getUser());
    }


}
