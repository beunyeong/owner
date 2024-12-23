package com.example.oner.entity;

import com.example.oner.enums.UserRole;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

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
    private UserRole role;

    private LocalDateTime deletedAt;

    public User() {}

    public User(String name ,String email, String password, UserRole role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public void setDeletedAt(){
        this.deletedAt = LocalDateTime.now();
    }
}
