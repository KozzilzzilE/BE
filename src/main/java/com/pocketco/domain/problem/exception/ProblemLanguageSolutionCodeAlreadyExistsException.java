package com.pocketco.domain.problem.exception;

import com.pocketco.global.common.code.status.ErrorStatus;
import com.pocketco.global.exception.GeneralException;

public class ProblemLanguageSolutionCodeAlreadyExistsException extends GeneralException {
    public ProblemLanguageSolutionCodeAlreadyExistsException() { super(ErrorStatus.PROBLEM_LANGUAGE_SOLUTION_CODE_ALREADY_EXISTS); }
}