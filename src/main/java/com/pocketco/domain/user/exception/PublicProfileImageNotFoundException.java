package com.pocketco.domain.user.exception;

import com.pocketco.global.common.code.status.ErrorStatus;
import com.pocketco.global.exception.GeneralException;

public class PublicProfileImageNotFoundException extends GeneralException {
    public PublicProfileImageNotFoundException() { super(ErrorStatus.PUBLIC_PROFILE_IMAGE_NOT_FOUND); }
}