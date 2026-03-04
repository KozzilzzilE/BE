package com.pocketco.domain.problem.exception;

import com.pocketco.global.common.code.BaseErrorCode;
import com.pocketco.global.exception.GeneralException;

public class ProblemHandler extends GeneralException {
    public ProblemHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}