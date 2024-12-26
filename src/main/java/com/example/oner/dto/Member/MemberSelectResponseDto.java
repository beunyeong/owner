package com.example.oner.dto.Member;

import com.example.oner.entity.Member;
import com.example.oner.enums.MemberRole;
import com.example.oner.enums.UserRole;
import lombok.Getter;

@Getter
public class MemberSelectResponseDto {
        private final Long workspaceId;
        private final String name;
        private final UserRole userRole;
        private final MemberRole memberRole;

        public MemberSelectResponseDto(Long workspaceId, String name, UserRole userRole, MemberRole memberRole){
            this.workspaceId = workspaceId;
            this.name = name;
            this.userRole = userRole;
            this.memberRole = memberRole;
        }

    public MemberSelectResponseDto(Member member){
            this.workspaceId = member.getWorkspace().getId();
            this.name = member.getUser().getName();
            this.userRole = member.getUser().getUserRole();
            this.memberRole = member.getRole();
    }
}
