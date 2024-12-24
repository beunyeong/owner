package com.example.oner.controller;


import com.example.oner.config.Const;
import com.example.oner.dto.User.LoginRequestDto;
import com.example.oner.dto.User.LoginResponseDto;
import com.example.oner.dto.User.UserRequestDto;
import com.example.oner.dto.User.UserResponseDto;
import com.example.oner.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<LoginResponseDto> loginUser(
            @Valid @RequestBody LoginRequestDto loginRequestDto, // 로그인 요청 데이터
            HttpServletRequest servletRequest){

        LoginResponseDto loginResponseDto = userService.login(loginRequestDto);
        // 세션 생성 및 사용자 정보 저장
        HttpSession httpSession = servletRequest.getSession();
        httpSession.setAttribute(Const.LOGIN_USER, loginResponseDto);

        return new ResponseEntity<>(loginResponseDto, HttpStatus.OK);
    }

    //회원 조회
    @GetMapping("/{userId}")
    public ResponseEntity<LoginResponseDto> getUser(@PathVariable Long userId){
        return new ResponseEntity<>(userService.getUser(userId), HttpStatus.OK);
    }

    @DeleteMapping("/resign/{userId}")
    public ResponseEntity<String> deactivateUser(
            @PathVariable Long userId,
            HttpServletRequest servletRequest
    ){
        HttpSession session = servletRequest.getSession();
        LoginResponseDto loginUser = (LoginResponseDto) session.getAttribute(Const.LOGIN_USER);

        userService.resignUser(userId , loginUser);
        return ResponseEntity.ok("회원 탈퇴되었습니다.");
    }
}
