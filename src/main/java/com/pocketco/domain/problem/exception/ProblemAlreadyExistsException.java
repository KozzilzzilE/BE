package com.pocketco.domain.problem.exception;

import com.pocketco.global.common.code.status.ErrorStatus;
import com.pocketco.global.exception.GeneralException;

public class ProblemAlreadyExistsException extends GeneralException {
    public ProblemAlreadyExistsException() { super(ErrorStatus.PROBLEM_ALREADY_EXISTS); }
}