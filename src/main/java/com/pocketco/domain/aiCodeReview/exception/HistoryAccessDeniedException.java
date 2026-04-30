package com.pocketco.domain.aiCodeReview.exception;

import com.pocketco.global.common.code.status.ErrorStatus;
import com.pocketco.global.exception.GeneralException;

public class HistoryAccessDeniedException extends GeneralException {
    public HistoryAccessDeniedException() { super(ErrorStatus.HISTORY_UNAUTHORIZED); }
}