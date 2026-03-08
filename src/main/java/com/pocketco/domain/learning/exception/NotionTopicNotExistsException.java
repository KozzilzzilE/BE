package com.pocketco.domain.learning.exception;

import com.pocketco.global.common.code.status.ErrorStatus;
import com.pocketco.global.exception.GeneralException;

public class NotionTopicNotExistsException extends GeneralException {
    public NotionTopicNotExistsException() { super(ErrorStatus.LEARNING_NOTION_TOPIC_NOT_EXISTS); }
}