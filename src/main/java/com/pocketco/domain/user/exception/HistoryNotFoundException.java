package com.pocketco.domain.user.exception;

import com.pocketco.global.common.code.status.ErrorStatus;
import com.pocketco.global.exception.GeneralException;

public class HistoryNotFoundException extends GeneralException {
    public HistoryNotFoundException() {
        super(ErrorStatus.HISTORY_NOT_FOUND);
    }
}