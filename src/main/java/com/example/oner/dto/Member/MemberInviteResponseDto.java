package com.example.oner.dto.Member;


import com.example.oner.enums.MemberRole;
import com.example.oner.enums.MemberWait;
import lombok.Getter;

@Getter
public class MemberInviteResponseDto {

    private final Long memberId;
    private final Long workspaceId;
    private final MemberRole role;
    private final MemberWait wait;

    public MemberInviteResponseDto(Long memberId, Long workspaceId, MemberRole role, MemberWait wait) {
        this.memberId = memberId;
        this.workspaceId = workspaceId;
        this.role = role;
        this.wait = wait;
    }
}
