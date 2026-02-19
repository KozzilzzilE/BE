package com.pocketco.domain.admin.exception;

import com.pocketco.global.common.code.status.ErrorStatus;
import com.pocketco.global.exception.GeneralException;

public class NotAdminException extends GeneralException {
    public NotAdminException() {
        super(ErrorStatus.ADMIN_IS_NOT);
    }
}
