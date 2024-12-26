package com.example.oner.dto.Member;

import com.example.oner.enums.MemberRole;
import lombok.Getter;

@Getter
public class MemberRoleUpdateRequestDto {
    private final Long workspaceId;
    private final MemberRole role;

    public MemberRoleUpdateRequestDto(Long workspaceId, MemberRole role) {
        this.workspaceId = workspaceId;
        this.role = role;
    }
}
