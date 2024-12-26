package com.example.oner.controller;


import com.example.oner.dto.Member.MemberCreateRequestDto;
import com.example.oner.dto.Member.MemberCreateResponseDto;
import com.example.oner.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/invitations/{userid}")
    public ResponseEntity<MemberCreateResponseDto> createMember (
            @PathVariable Long userid,
            @RequestBody MemberCreateRequestDto memberCreateRequestDto,
            HttpServletRequest request) {

        return memberService.createMember(userid,request,memberCreateRequestDto);
    }
}
