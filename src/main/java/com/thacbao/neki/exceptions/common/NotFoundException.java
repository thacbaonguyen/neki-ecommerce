package com.thacbao.neki.exceptions.common;

import com.thacbao.neki.exceptions.ErrorCode;

public class NotFoundException extends AppException {
    public NotFoundException(String message) {
        super(ErrorCode.NOT_FOUND, message);
    }

    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}