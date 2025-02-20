package com.example.oner.config;

import org.springframework.stereotype.Component;
import java.util.regex.Pattern;

@Component
public class SignUpValidation {

    // 비밀번호 유효성 검사 패턴
    // - 최소 8자 이상
    // - 대문자 하나 이상 포함
    // - 소문자 하나 이상 포함
    // - 숫자 하나 이상 포함
    // - 특수문자 하나 이상 포함
    private final String PASSWORD_PATTERN =
            "^(?=.*[A-Z])(?=.*[0-9])(?=.*[a-z])(?=.*[!@#$%^&*()-+=]).{8,}$";

    // 이메일 유효성 검사 패턴
    private final String EMAIL_PATTERN =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    // 정규식 패턴 객체 생성
    private final Pattern passwordPattern = Pattern.compile(PASSWORD_PATTERN);
    private final Pattern emailPattern = Pattern.compile(EMAIL_PATTERN);


    public boolean isValidPassword(String password) {
        // 비밀번호가 null인지 확인, 비밀번호가 패턴에 매칭되는지 확인
        return password != null && passwordPattern.matcher(password).matches();
    }

    public boolean isValidEmail(String email) {
        // 비밀번호가 null인지 확인, 이메일이 패턴에 매칭되는지 확인
        return email != null && emailPattern.matcher(email).matches();
    }
}
