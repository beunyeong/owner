package com.example.oner.error.exception;

import com.example.oner.error.errorcode.ErrorCode;

public class BadRequestException extends CustomException {
    public BadRequestException() {
        super(ErrorCode.INVALID_ROLE);
    }
}
