package com.pocketco.domain.learning.exception;

import com.pocketco.global.common.code.status.ErrorStatus;
import com.pocketco.global.exception.GeneralException;

public class AppliedExerciseNotExistsException extends GeneralException {
    public AppliedExerciseNotExistsException() { super(ErrorStatus.LEARNING_APPLIED_EXERCISE_NOT_EXISTS); }
}