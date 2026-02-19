package com.pocketco.domain.auth.exception;

import com.pocketco.global.common.code.status.ErrorStatus;
import com.pocketco.global.exception.GeneralException;

public class AuthAlreadyUsedException extends GeneralException {
    public AuthAlreadyUsedException() {
        super(ErrorStatus.AUTH_ALREADY_USED);
    }
}
