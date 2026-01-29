package com.thacbao.neki.exceptions.user;

import com.thacbao.neki.exceptions.ErrorCode;
import com.thacbao.neki.exceptions.common.AppException;

public class PermissionException extends AppException {
    public PermissionException(String message) {
        super(ErrorCode.PERMISSION_DENIED, message);
    }

    public PermissionException(ErrorCode errorCode) {
        super(errorCode);
    }
}