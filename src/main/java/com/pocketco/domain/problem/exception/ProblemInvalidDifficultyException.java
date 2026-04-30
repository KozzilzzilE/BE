package com.pocketco.domain.problem.exception;

import com.pocketco.global.common.code.status.ErrorStatus;
import com.pocketco.global.exception.GeneralException;

public class ProblemInvalidDifficultyException extends GeneralException {
    public ProblemInvalidDifficultyException() {
        super(ErrorStatus.PROBLEM_INVALID_DIFFICULTY);
    }
}