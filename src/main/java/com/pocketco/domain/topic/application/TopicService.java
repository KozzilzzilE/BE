package com.pocketco.domain.topic.application;

import com.pocketco.domain.admin.dto.AddTopicRequest;
import com.pocketco.domain.admin.dto.AddTopicResponse;

public interface TopicService {
    AddTopicResponse addTopic(AddTopicRequest req);
}