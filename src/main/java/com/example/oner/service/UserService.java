package com.example.oner.service;

import com.example.oner.config.PasswordEncoder;
import com.example.oner.config.SignUpValidation;
import com.example.oner.dto.User.LoginRequestDto;
import com.example.oner.dto.User.LoginResponseDto;
import com.example.oner.dto.User.UserRequestDto;
import com.example.oner.dto.User.UserResponseDto;
import com.example.oner.entity.User;
import com.example.oner.enums.UserRole;
import com.example.oner.enums.UserStatus;
import com.example.oner.error.errorcode.ErrorCode;
import com.example.oner.error.exception.CustomException;
import com.example.oner.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SignUpValidation signUpValidation;

    //회원가입
    public UserResponseDto signUp(UserRequestDto requestDto){

        // 이메일 형식 확인
        if (!signUpValidation.isValidEmail(requestDto.getEmail())){
            throw new CustomException(ErrorCode.EMAIL_FORM_ERROR);
        }
        // 비밀펀호 패턴 확인
        if (!signUpValidation.isValidPassword(requestDto.getPassword())){
            throw new CustomException(ErrorCode.PASSWORD_PATTERN_ERROR);
        }
        // 이메일 중복유무 확인
        if (userRepository.findUserByEmail(requestDto.getEmail()).isPresent()){
            throw new CustomException(ErrorCode.DUPLICATE_RESOURCE);
        }

        User user = new User(requestDto);
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        User saveUser = userRepository.save(user);

        return new UserResponseDto(saveUser);
    }

    //로그인
    public LoginResponseDto login(LoginRequestDto requestDto){

        User findUser = userRepository.findUserByEmail(requestDto.getEmail())
                .orElseThrow(()->new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(requestDto.getPassword(),findUser.getPassword())){
            throw new CustomException(ErrorCode.PASSWORD_ERROR);
        }
        return new LoginResponseDto(findUser);
    }

    public LoginResponseDto getUser(Long userId){
        User findUser = userRepository.findByIdOrElseThrow(userId);
        return new LoginResponseDto(findUser);
    }

    //회원 탈퇴
    @Transactional
    public void resignUser(Long userId , LoginResponseDto responseDto){

        //로그인유저와 탈퇴 요청유저가 일치하는지 확인
        if (!Objects.equals(userId, responseDto.getUserId())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_USER);
        }

        User findUser = userRepository.findByIdOrElseThrow(responseDto.getUserId());
        findUser.setStatus(UserStatus.DEACTIVATED);
        userRepository.save(findUser);
    }



}
