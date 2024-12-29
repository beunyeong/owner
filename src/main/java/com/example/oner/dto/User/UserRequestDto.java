package com.example.oner.dto.User;

import com.example.oner.enums.UserRole;
import lombok.Getter;

@Getter
public class UserRequestDto {

    private String name;

    private String email;

    private String password;

    private UserRole userRole;

    public UserRequestDto(String name,  String email, String password , UserRole userRole) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.userRole = userRole;
    }
}
