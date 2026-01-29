package com.thacbao.neki.exceptions.common;

import com.thacbao.neki.exceptions.ErrorCode;

public class InvalidException extends AppException {
    public InvalidException(String message) {
        super(ErrorCode.INVALID_INPUT, message);
    }

    public InvalidException(ErrorCode errorCode) {
        super(errorCode);
    }
}