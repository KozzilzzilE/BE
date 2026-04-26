package com.pocketco.domain.problem.exception;

import com.pocketco.global.common.code.status.ErrorStatus;
import com.pocketco.global.exception.GeneralException;

public class ProblemLanguageTimeLimitAlreadyExistsException extends GeneralException {
    public ProblemLanguageTimeLimitAlreadyExistsException() { super(ErrorStatus.PROBLEM_LANGUAGE_TIME_LIMIT_ALREADY_EXISTS); }
}