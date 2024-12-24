package com.example.oner.entity;

import com.example.oner.config.PasswordEncoder;
import com.example.oner.dto.User.UserRequestDto;
import com.example.oner.enums.UserRole;
import com.example.oner.enums.UserStatus;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "users")
public class User extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
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

    public User() {}

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

}
