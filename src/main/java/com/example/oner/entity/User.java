package com.example.oner.entity;

import com.example.oner.dto.User.UserRequestDto;
import com.example.oner.enums.UserRole;
import com.example.oner.enums.UserStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
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

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = true)
    private Member member;

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

    public Member getMember() {
        return this.member;
    }

}
