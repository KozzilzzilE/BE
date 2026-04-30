package com.pocketco.domain.problem.exception;

import com.pocketco.global.common.code.status.ErrorStatus;
import com.pocketco.global.exception.GeneralException;

public class ProblemNotFoundException extends GeneralException {
    public ProblemNotFoundException() {
        super(ErrorStatus.PROBLEM_NOT_FOUND); // ✅ 실제 등록된 상수명
    }
}