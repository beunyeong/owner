package com.example.oner.dto.User;

import com.example.oner.entity.User;
import com.example.oner.enums.UserRole;
import com.example.oner.enums.UserStatus;
import lombok.Getter;

@Getter
public class ResignResponseDto {
    private final Long userId;

    private final String name;

    private final UserStatus userStatus;

    public ResignResponseDto(Long userId, String name , UserStatus userStatus) {
        this.userId = userId;
        this.name = name;
        this.userStatus = userStatus;
    }

    public ResignResponseDto(User user){
        this.userId = user.getId();
        this.name = user.getName();
        this.userStatus = user.getUserStatus();
    }
}
