package com.example.oner.dto.Member;

import com.example.oner.enums.MemberRole;
import lombok.Getter;

@Getter
public class MemberDetailDto {

    private final String name;
    private final Long userId;
    private final MemberRole role;

    public MemberDetailDto(String name, Long userId, MemberRole role) {
        this.name = name;
        this.userId = userId;
        this.role = role;
    }

}
