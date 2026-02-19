package com.pocketco.domain.topic.exception;

import com.pocketco.global.common.code.status.ErrorStatus;
import com.pocketco.global.exception.GeneralException;

public class TopicNotFoundException extends GeneralException {
    public TopicNotFoundException() {
        super(ErrorStatus.TOPIC_NOT_FOUND);
    }
}
