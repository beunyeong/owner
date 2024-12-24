package com.example.oner.entity;


import com.example.oner.enums.MemberRole;
import com.example.oner.enums.MemberWait;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "member")
public class Members extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id")
    private Workspace workspace;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MemberRole role;

    @Enumerated(EnumType.STRING)
    private MemberWait wait;

    public Members(){}

    public Members(User user, Workspace workspace, MemberRole role, MemberWait wait) {
        this.user = user;
        this.workspace = workspace;
        this.role = role;
        this.wait = wait;
    }






}
