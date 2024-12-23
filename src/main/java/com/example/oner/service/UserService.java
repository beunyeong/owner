package com.example.oner.service;

import com.example.oner.config.PasswordEncoder;
import com.example.oner.config.SignUpValidation;
import com.example.oner.dto.User.UserRequestDto;
import com.example.oner.dto.User.UserResponseDto;
import com.example.oner.entity.User;
import com.example.oner.enums.UserRole;
import com.example.oner.enums.UserStatus;
import com.example.oner.error.errorcode.ErrorCode;
import com.example.oner.error.exception.CustomException;
import com.example.oner.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SignUpValidation signUpValidation;

    @Transactional
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
}
