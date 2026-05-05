package com.pocketco.domain.learning.exception;

import com.pocketco.global.common.code.status.ErrorStatus;
import com.pocketco.global.exception.GeneralException;

public class AppliedCodeAlreadyExistsException extends GeneralException {
    public AppliedCodeAlreadyExistsException() {
        super(ErrorStatus.LEARNING_APPLIED_CODE_ALREADY_EXISTS);
    }
}