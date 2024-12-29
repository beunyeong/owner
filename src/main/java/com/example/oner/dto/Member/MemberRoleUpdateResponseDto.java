package com.example.oner.dto.Member;

import com.example.oner.enums.MemberRole;
import lombok.Getter;

@Getter
public class MemberRoleUpdateResponseDto {
    private final String message;
    private final Long memberId;
    private final Long workspaceId;
    private final MemberRole role;

    public MemberRoleUpdateResponseDto(String message, Long memberId, Long workspaceId, MemberRole role) {
        this.message = message;
        this.memberId = memberId;
        this.workspaceId = workspaceId;
        this.role = role;
    }
}
