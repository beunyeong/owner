package com.example.oner.entity;

import com.example.oner.enums.MemberRole;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class UserRoleMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private MemberRole memberRole;

    private Long resourceId; // 리소스 ID (보드나 리스트 ID)

    private String resourceType; // 리소스 타입 (BOARD, LIST 등)

    public UserRoleMapping() {}

    public UserRoleMapping(User user, MemberRole memberRole, Long resourceId, String resourceType) {
        this.user = user;
        this.memberRole = memberRole;
        this.resourceId = resourceId;
        this.resourceType = resourceType;
    }

}

