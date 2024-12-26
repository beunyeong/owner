package com.example.oner.dto.Member;


import com.example.oner.enums.MemberRole;
import com.example.oner.enums.MemberWait;
import lombok.Getter;

@Getter
public class MemberCreateResponseDto {

    private Long memberId;
    private Long workspaceId;
    private MemberRole role;
    private MemberWait wait;

    public MemberCreateResponseDto() {}

    public MemberCreateResponseDto(Long memberId, Long workspaceId, MemberRole role) {
        this.memberId = memberId;
        this.workspaceId = workspaceId;
        this.role = role;
        this.wait = MemberWait.WAIT;

    }
}
