package com.example.oner.controller;


import com.example.oner.dto.JwtAuthResponse;
import com.example.oner.dto.User.*;
import com.example.oner.dto.common.CommonResponseBody;
import com.example.oner.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {


    private final UserService userService;
    private final AuthenticationManager authenticationManager;

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
    public ResponseEntity<LoginResponseDto> getUser(@PathVariable Long userId){
        return new ResponseEntity<>(userService.getUser(userId), HttpStatus.OK);
    }

    @DeleteMapping("/{userId}/resign")
    public ResponseEntity<String> deactivateUser(
            @PathVariable Long userId
    ){
        userService.resignUser(userId);
        return ResponseEntity.ok("회원 탈퇴되었습니다.");
    }


}
