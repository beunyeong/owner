package com.example.oner.dto.User;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LoginRequestDto {

    @NotBlank(message = "사용자 ID를 입력해주세요")
    @Column(nullable = false)
    private final String email;

    @NotBlank(message = "비밀번호를 입력해주세요")
    @Column(nullable = false)
    private final String password;

    public LoginRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}