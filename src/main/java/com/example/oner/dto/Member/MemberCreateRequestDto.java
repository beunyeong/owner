package com.example.oner.dto.Member;


import com.example.oner.enums.MemberRole;
import lombok.Getter;

@Getter
public class MemberCreateRequestDto {

    private final Long workspaceId;
    private final MemberRole role;

    public MemberCreateRequestDto(Long workspaceId, MemberRole role) {
        this.workspaceId = workspaceId;
        this.role = role;
    }

}
