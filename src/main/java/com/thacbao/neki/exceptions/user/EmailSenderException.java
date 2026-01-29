package com.thacbao.neki.exceptions.user;

import com.thacbao.neki.exceptions.ErrorCode;
import com.thacbao.neki.exceptions.common.AppException;

public class EmailSenderException extends AppException {
    public EmailSenderException(String message) {
        super(ErrorCode.EMAIL_SEND_FAILED, message);
    }

    public EmailSenderException(Throwable cause) {
        super(ErrorCode.EMAIL_SEND_FAILED, ErrorCode.EMAIL_SEND_FAILED.getMessage(), cause);
    }
}