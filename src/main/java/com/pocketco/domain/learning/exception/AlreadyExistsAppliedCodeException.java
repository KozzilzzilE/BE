package com.pocketco.domain.learning.exception;

import com.pocketco.global.common.code.status.ErrorStatus;
import com.pocketco.global.exception.GeneralException;

public class AlreadyExistsAppliedCodeException extends GeneralException {
    public AlreadyExistsAppliedCodeException() { super(ErrorStatus.LEARNING_APPLIED_CODE_ALREADY_EXISTS); }
}