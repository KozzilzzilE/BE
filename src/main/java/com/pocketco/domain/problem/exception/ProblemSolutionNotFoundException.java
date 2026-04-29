package com.pocketco.domain.problem.exception;

import com.pocketco.global.common.code.status.ErrorStatus;
import com.pocketco.global.exception.GeneralException;

public class ProblemSolutionNotFoundException extends GeneralException {
    public ProblemSolutionNotFoundException() { super(ErrorStatus.SOLUTION_NOT_FOUND); }
}