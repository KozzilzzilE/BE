package com.pocketco.domain.learning.exception;

import com.pocketco.global.common.code.status.ErrorStatus;
import com.pocketco.global.exception.GeneralException;

public class AppliedExerciseTopicNotExistsException extends GeneralException {
    public AppliedExerciseTopicNotExistsException() { super(ErrorStatus.LEARNING_APPLIED_EXERCISE_TOPIC_NOT_EXISTS); }
}