package com.pocketco.domain.learning.exception;

import com.pocketco.global.common.code.status.ErrorStatus;
import com.pocketco.global.exception.GeneralException;

public class AlreadyExistsAppliedExerciseException extends GeneralException {
    public AlreadyExistsAppliedExerciseException() { super(ErrorStatus.LEARNING_APPLIED_ALREADY_EXISTS); }
}