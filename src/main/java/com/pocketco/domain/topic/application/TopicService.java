package com.pocketco.domain.topic.application;

import com.pocketco.domain.admin.dto.AddTopicRequest;
import com.pocketco.domain.admin.dto.AddTopicResponse;
import com.pocketco.domain.topic.dto.TopicListResponseDTO;

public interface TopicService {
    AddTopicResponse addTopic(AddTopicRequest req);

    TopicListResponseDTO getTopicList();
}