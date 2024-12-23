package com.example.oner.controller;


import com.example.oner.dto.User.UserRequestDto;
import com.example.oner.dto.User.UserResponseDto;
import com.example.oner.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {


    private final UserService userService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserRequestDto userDto) {
        return new ResponseEntity<>(userService.signUp(userDto), HttpStatus.CREATED);
    }
}
