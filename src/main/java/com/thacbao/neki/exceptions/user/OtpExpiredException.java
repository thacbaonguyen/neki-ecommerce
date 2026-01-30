package com.thacbao.neki.exceptions.user;

import com.thacbao.neki.exceptions.ErrorCode;
import com.thacbao.neki.exceptions.common.AppException;

public class OtpExpiredException extends AppException {
    public OtpExpiredException(String message) {
        super(ErrorCode.OTP_EXPIRED, message);
    }

    public OtpExpiredException(ErrorCode errorCode) {
        super(errorCode);
    }
}