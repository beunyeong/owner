package com.example.oner.dto.Member;


import lombok.Getter;

@Getter
public class MemberRequestDto {

    private final Long workspaceId;

    public MemberRequestDto( Long workspaceId){
        this.workspaceId = workspaceId;
    }
}
