package com.example.oner.dto.Member;

import lombok.Getter;

import java.util.List;

@Getter
public class MemberResponseDto {

    private final Long workspaceId;
    private final List<MemberDetailDto> members;

    public MemberResponseDto(Long workspaceId, List<MemberDetailDto> members) {
        this.workspaceId = workspaceId;
        this.members = members;
    }



}
