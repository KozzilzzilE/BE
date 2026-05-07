package com.pocketco.domain.problem.exception;

import com.pocketco.global.exception.GeneralException;
import com.pocketco.global.common.code.status.ErrorStatus;

public class TempStorageNotFoundException extends GeneralException {
    public TempStorageNotFoundException() {
        super(ErrorStatus.TEMP_STORAGE_NOT_FOUND);
    }
}