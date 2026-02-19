package com.pocketco.domain.topic.exception;

import com.pocketco.global.common.code.status.ErrorStatus;
import com.pocketco.global.exception.GeneralException;

public class TopicAlreadyUsedException extends GeneralException {
    public TopicAlreadyUsedException() {
        super(ErrorStatus.TOPIC_ALREADY_USED);
    }
}
