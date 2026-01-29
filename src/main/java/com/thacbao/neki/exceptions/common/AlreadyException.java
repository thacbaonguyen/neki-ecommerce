package com.thacbao.neki.exceptions.common;

import com.thacbao.neki.exceptions.ErrorCode;

public class AlreadyException extends AppException {
    public AlreadyException(String message) {
        super(ErrorCode.ALREADY_EXISTS, message);
    }

    public AlreadyException(ErrorCode errorCode) {
        super(errorCode);
    }
}