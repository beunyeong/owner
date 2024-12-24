package com.example.oner.dto.User;

import com.example.oner.entity.User;
import com.example.oner.enums.UserRole;
import lombok.Getter;

@Getter
public class LoginResponseDto {

    private final Long userId;

    private final String name;

    private final UserRole userRole;

    public LoginResponseDto(Long userId, String name , UserRole userRole) {
        this.userId = userId;
        this.name = name;
        this.userRole = userRole;
    }

    public LoginResponseDto(User user){
        this.userId = user.getId();
        this.name = user.getName();
        this.userRole = user.getUserRole();
    }
}
