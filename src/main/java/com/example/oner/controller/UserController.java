package com.example.oner.controller;


import com.example.oner.config.auth.UserDetailsImpl;
import com.example.oner.dto.JwtAuthResponse;
import com.example.oner.dto.user.*;
import com.example.oner.dto.common.CommonResponseBody;
import com.example.oner.entity.User;
import com.example.oner.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    // 사용자 로그인
    @PostMapping("/login")
    public ResponseEntity<CommonResponseBody<JwtAuthResponse>> login(
            @Valid @RequestBody AccountRequest accountRequest) {
        JwtAuthResponse authResponse = this.userService.login(accountRequest);

        return ResponseEntity.ok(new CommonResponseBody<>("로그인 성공", authResponse));
    }

    //회원 조회
    @GetMapping("/{userId}")
    public ResponseEntity<LoginResponseDto> getUser(@PathVariable Long userId ,
                                                    @AuthenticationPrincipal UserDetailsImpl userDetails){
        return new ResponseEntity<>(userService.getUser(userId , userDetails.getUser()), HttpStatus.OK);
    }

    @DeleteMapping("/{userId}/resign")
    public ResponseEntity<ResignResponseDto> deactivateUser(
            @PathVariable Long userId,
            @AuthenticationPrincipal UserDetailsImpl userDetails){
        return new ResponseEntity<>(userService.resignUser(userId , userDetails.getUser()) , HttpStatus.OK);
    }


}
