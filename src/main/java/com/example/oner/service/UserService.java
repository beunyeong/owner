package com.example.oner.service;

import com.example.oner.config.SignUpValidation;
import com.example.oner.config.auth.UserDetailsImpl;
import com.example.oner.dto.JwtAuthResponse;
import com.example.oner.dto.user.*;
import com.example.oner.entity.User;
import com.example.oner.enums.UserStatus;

import com.example.oner.error.errorcode.ErrorCode;
import com.example.oner.error.exception.CustomException;
import com.example.oner.repository.UserRepository;
import com.example.oner.util.AuthenticationScheme;
import com.example.oner.util.JwtProvider;
import com.slack.api.methods.SlackApiException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Objects;



@Service
@RequiredArgsConstructor
@Slf4j(topic = "Security::AccountService")
public class UserService {

    private final UserRepository userRepository;
    private final SignUpValidation signUpValidation;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final SlackService slackService;

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
        user.setPassword(bCryptPasswordEncoder.encode(requestDto.getPassword()));
        User saveUser = userRepository.save(user);

        String message = saveUser.getName()+"님께서 회원가입 하셨습니다.";
        sendMessageFromSlack(message);

        return new UserResponseDto(saveUser);
    }



    //로그인
    public JwtAuthResponse login(AccountRequest accountRequest) {
        User user = this.userRepository.findByEmail(accountRequest.getEmail())
                .orElseThrow(()->new CustomException(ErrorCode.USER_NOT_FOUND));
        this.validatePassword(accountRequest.getPassword(), user.getPassword());

        // 사용자 인증 후 인증 객체를 저장
        Authentication authentication = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        accountRequest.getEmail(),
                        accountRequest.getPassword())
        );
        // 토큰 생성
        String accessToken = this.jwtProvider.generateToken(authentication);
        log.info("토큰 생성: {}", accessToken);
        return new JwtAuthResponse(AuthenticationScheme.BEARER.getName(), accessToken);
    }

    //회원 조회
    public LoginResponseDto getUser(Long userId , User loginUser){
        User findUser = userRepository.findByIdOrElseThrow(userId);
        log.info(loginUser.getName());
        return new LoginResponseDto(findUser);
    }

    //회원 탈퇴
    @Transactional
    public ResignResponseDto resignUser(Long userId , User loginUser){


        //로그인유저와 탈퇴 요청유저가 일치하는지 확인
        if (!Objects.equals(userId , loginUser.getId())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_USER);
        }

        User findUser = userRepository.findByIdOrElseThrow(loginUser.getId());
        findUser.setStatus(UserStatus.DEACTIVATED);
        userRepository.save(findUser);
        return new ResignResponseDto(findUser);
    }

    private void validatePassword(String rawPassword, String encodedPassword)
            throws IllegalArgumentException {
        boolean notValid = !this.bCryptPasswordEncoder.matches(rawPassword, encodedPassword);
        if (notValid) {
            throw new CustomException(ErrorCode.PASSWORD_ERROR);
        }
    }

    private void sendMessageFromSlack(String message) {
        try {
            slackService.sendMessage(message);
            log.info("Message sent successfully!");
        } catch (IOException | SlackApiException e) {
            throw new CustomException(ErrorCode.MESSAGE_SENDING_ERROR);
        }
    }

}
