package com.example.oner.dto.Member;

import com.example.oner.enums.MemberWait;
import lombok.Getter;

@Getter
public class MemberInviteRequestDto {

    public final Long workspaceId;
    public final MemberWait wait;


    public MemberInviteRequestDto(Long workspaceId,MemberWait wait) {
    this.workspaceId = workspaceId;
    this.wait = wait;}

}
