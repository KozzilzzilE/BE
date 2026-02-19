package com.pocketco.domain.topic.application;

import com.pocketco.domain.admin.dto.AddTopicRequest;
import com.pocketco.domain.admin.dto.AddTopicResponse;
import com.pocketco.domain.language.exception.AlreadyExistsLanguageException;
import com.pocketco.domain.topic.entity.Topic;
import com.pocketco.domain.topic.exception.TopicAlreadyUsedException;
import com.pocketco.domain.topic.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TopicServiceImpl implements TopicService {
    private final TopicRepository topicRepository;

    private void existsTopic(String name, String displayName) {
        if (topicRepository.existsByName(name) || topicRepository.existsByDisplayName(displayName)) {
            throw new TopicAlreadyUsedException();
        }
    }

    @Override
    public AddTopicResponse addTopic(AddTopicRequest req) {
        existsTopic(req.name(), req.displayName());

        Topic topic = Topic.builder()
                .name(req.name())
                .displayName(req.displayName())
                .build();
        Topic saved = topicRepository.save(topic);

        return AddTopicResponse.builder()
                .topicId(saved.getId())
                .build();
    }
}