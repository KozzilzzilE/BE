package com.pocketco.domain.learning.exception;

import com.pocketco.global.common.code.status.ErrorStatus;
import com.pocketco.global.exception.GeneralException;

public class AppliedCodeAnswerDuplicateException extends GeneralException {
    public AppliedCodeAnswerDuplicateException() {
        super(ErrorStatus.LEARNING_APPLIED_CODE_ANSWER_DUPLICATE);
    }
}