package com.pocketco.domain.admin.application;

import com.pocketco.domain.admin.dto.AddLanguageRequest;
import com.pocketco.domain.admin.dto.AddLanguageResponse;
import com.pocketco.domain.admin.dto.AddTopicRequest;
import com.pocketco.domain.admin.dto.AddTopicResponse;
import com.pocketco.domain.admin.exception.NotAdminException;
import com.pocketco.domain.language.application.LanguageService;
import com.pocketco.domain.topic.application.TopicService;
import com.pocketco.domain.user.entity.Role;
import com.pocketco.domain.user.entity.User;
import com.pocketco.domain.user.exception.UserNotFoundException;
import com.pocketco.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;
    private final LanguageService languageService;
    private final TopicService topicService;

    private void validateAdmin(Long userId) {
        User me = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        if (me.getRole() != Role.ADMIN) {
            throw new NotAdminException();
        }
    }

    @Override
    public AddLanguageResponse addLanguage(Long userId, AddLanguageRequest request) {
        validateAdmin(userId);
        return languageService.addLanguage(request);
    }

    @Override
    public AddTopicResponse addTopic(Long userId, AddTopicRequest request) {
        validateAdmin(userId);
        return topicService.addTopic(request);
    }
}
