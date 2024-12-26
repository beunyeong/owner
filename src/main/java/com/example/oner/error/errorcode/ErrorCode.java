package com.example.oner.error.errorcode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    /* 400 BAD_REQUEST : 잘못된 요청 */
    EMAIL_FORM_ERROR(BAD_REQUEST, "이메일 형식 에러"),
    PASSWORD_PATTERN_ERROR(BAD_REQUEST, "비밀번호 패턴 에러"),
    INVALID_ROLE(BAD_REQUEST, "잘못된 역할입니다."),
    MESSAGE_SENDING_ERROR(BAD_REQUEST,"message send error"),


    /* 401 UNAUTHORIZED : 인증되지 않은 사용자 */
    UNAUTHORIZED_USER(UNAUTHORIZED, "권한이 없습니다. 해당유저만 가능합니다."),
    PASSWORD_ERROR(UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),

    /* 403 FORBIDDEN : 권한이 없음 */
    INVALID_USER_ROLE(FORBIDDEN, "사장님만 가능한 작업입니다."),




    /* 404 NOT_FOUND : Resource 를 찾을 수 없음 */
    USER_NOT_FOUND(NOT_FOUND, "유저 정보를 찾을 수 없습니다."),
    COMMENT_NOT_FOUND(NOT_FOUND, "댓글 정보를 찾을 수 없습니다."),


    /* 409 CONFLICT : Resource 의 현재 상태와 충돌. 보통 중복된 데이터 존재 */
    DUPLICATE_RESOURCE(CONFLICT, "데이터가 이미 존재합니다"),

    ;

    private final HttpStatus httpStatus;
    private final String detail;
}
