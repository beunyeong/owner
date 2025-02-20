package com.example.oner.error.exception;

import com.example.oner.error.errorcode.ErrorCode;

public class MemberNotAuthorizedException extends CustomException {
    public MemberNotAuthorizedException(ErrorCode errorCode) {
        super(errorCode);
    }

    public MemberNotAuthorizedException(){
        super(ErrorCode.UNAUTHORIZED_USER);
    }
}
