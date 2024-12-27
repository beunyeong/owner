package com.example.oner.entity;


import com.example.oner.enums.MemberRole;
import com.example.oner.enums.MemberWait;
import jakarta.persistence.*;
import lombok.Getter;
import java.util.List;

@Entity
@Getter
@Table(name = "member")
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id", referencedColumnName = "id")
    private Workspace workspace;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MemberRole role;

    @Enumerated(EnumType.STRING)
    private MemberWait wait;

    @ManyToMany(mappedBy = "members")
    private List<Workspace> workspaces;

    public Member(){}

    public Member(User user, Workspace workspace, MemberRole role) {
        this.user = user;
        this.workspace = workspace;
        this.role = role;
        this.wait = MemberWait.WAIT;
    }


    public void setWait(MemberWait wait) {
        this.wait = wait;
    }

    public void setRole(MemberRole newRole) {
        this.role = newRole;
    }

    // 멤버가 워크스페이스에 속해있는지 확인
    public boolean isMemberOf(Workspace workspace) {
        return this.workspace != null && this.workspace.getId().equals(workspace.getId());
    }

    public boolean hasPermission(MemberRole requiredRole) {
        if (requiredRole == MemberRole.READ) {
            return true; // 읽기 권한은 기본 제공
        }
        return this.role.equals(requiredRole);
    }

}

