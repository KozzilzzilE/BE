package com.pocketco.domain.user.exception;

import com.pocketco.global.common.code.status.ErrorStatus;
import com.pocketco.global.exception.GeneralException;

public class UserNotFoundException extends GeneralException {
    public UserNotFoundException() {
        super(ErrorStatus.USER_NOT_FOUND);
    }
}
