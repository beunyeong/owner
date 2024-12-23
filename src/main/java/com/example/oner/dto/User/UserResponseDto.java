package com.example.oner.dto.User;

import com.example.oner.entity.User;
import com.example.oner.enums.UserRole;
import lombok.Getter;

@Getter
public class UserResponseDto {

    private final Long id;

    private final String name;

    private final String email;

    private final UserRole userRole;

    public UserResponseDto(Long id, String name, String email, UserRole userRole) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.userRole = userRole;
    }

    public UserResponseDto(User user){
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.userRole = user.getUserRole();
    }
}
