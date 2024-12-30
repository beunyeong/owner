package com.example.oner.entity;

import com.example.oner.dto.user.UserRequestDto;
import com.example.oner.enums.UserRole;
import com.example.oner.enums.UserStatus;
import com.example.oner.error.errorcode.ErrorCode;
import com.example.oner.error.exception.CustomException;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "users")
public class User extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    // USER, ADMIN
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private UserRole userRole;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private UserStatus userStatus = UserStatus.ACTIVE;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Member> member = new ArrayList<>();

    public User(){}

    public User(UserRequestDto requestDto){
        this.name = requestDto.getName();
        this.email = requestDto.getEmail();
        this.userRole = requestDto.getUserRole();
    }

    public User(String name ,String email, String password, UserRole userRole) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.userRole = userRole;
    }

    public void setStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public void setPassword(String encodePassword){
        this.password = encodePassword;
    }

    public boolean isAdmin() {
        return this.userRole == UserRole.ADMIN;
    }

    // 멤버 여부 확인
    public Member getMember() {
        if(this.member.isEmpty()) {
            throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
        }

        // 멤버가 있으면 첫번째 멤버 조회
        return this.member.stream()
                .findFirst().orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

    }

}
