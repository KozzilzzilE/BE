package com.pocketco.domain.learning.application;

import com.pocketco.domain.admin.dto.AddNotionCodeRequest;
import com.pocketco.domain.admin.dto.AddNotionRequest;
import com.pocketco.domain.admin.dto.AddNotionResponse;
import com.pocketco.domain.language.application.LanguageService;
import com.pocketco.domain.language.entity.Language;
import com.pocketco.domain.admin.converter.AdminConverter;
import com.pocketco.domain.learning.converter.LearningNotionConverter;
import com.pocketco.domain.learning.dto.LearningNotionCompletionResponse;
import com.pocketco.domain.learning.dto.LearningNotionResponse;
import com.pocketco.domain.learning.dto.LearningNotion;
import com.pocketco.domain.learning.entity.notion.Notion;
import com.pocketco.domain.learning.entity.notion.NotionCode;
import com.pocketco.domain.learning.entity.notion.NotionCompletion;
import com.pocketco.domain.learning.exception.AlreadyExistsNotionPageException;
import com.pocketco.domain.learning.exception.NotionNotExistsException;
import com.pocketco.domain.learning.exception.NotionTopicNotExistsException;
import com.pocketco.domain.learning.repository.notion.NotionCodeRepository;
import com.pocketco.domain.learning.repository.notion.NotionCompletionRepository;
import com.pocketco.domain.learning.repository.notion.NotionRepository;
import com.pocketco.domain.topic.entity.Topic;
import com.pocketco.domain.topic.exception.TopicNotFoundException;
import com.pocketco.domain.topic.repository.TopicRepository;
import com.pocketco.domain.user.entity.User;
import com.pocketco.domain.user.exception.UserNotFoundException;
import com.pocketco.domain.user.repository.UserRepository;
import com.pocketco.global.util.file.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class NotionServiceImpl implements NotionService {
    private final NotionRepository notionRepository;
    private final NotionCodeRepository notionCodeRepository;
    private final NotionCompletionRepository notionCompletionRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;
    private final TopicRepository topicRepository;
    private final LanguageService languageService;

    @Override
    public AddNotionResponse addNotion(MultipartFile image, AddNotionRequest req) throws IOException {
        if (notionRepository.existsByTopic_IdAndPageNo(req.topicId(), req.pageNo())) {
            throw new AlreadyExistsNotionPageException();
        }
        Topic topic = topicRepository.findById(req.topicId()).orElseThrow(TopicNotFoundException::new);
        String img = null;
        if (image != null && !image.isEmpty()) {
            img = fileStorageService.save(image, "notions/" + topic.getName());
        }

        Notion notion = Notion.builder()
                .topic(topic)
                .pageNo(req.pageNo())
                .title(req.title())
                .point(req.point())
                .detail(req.detail())
                .imgUrl(img)
                .build();

        Notion savedNotion = notionRepository.save(notion);

        // 중복된 언어 및 존재하지 않는 언어 추가 요청을 방지
        Map<Long, Language> languageMap = languageService.validateAndGetLanguageMap(
                req.codes().stream().map(AddNotionCodeRequest::languageId).toList()
        );

        List<NotionCode> codes = req.codes().stream()
                .map(reqCode -> NotionCode.builder()
                        .notion(savedNotion)
                        .language(languageMap.get(reqCode.languageId()))
                        .content(reqCode.content())
                        .build())
                .toList();

        notionCodeRepository.saveAll(codes);

        return AdminConverter.toAddNotionResponse(savedNotion, req.codes().size());
    }

    @Override
    public LearningNotionResponse getLearningNotions(Long topicId, String language, Long userId) {
        Long languageId = languageService.getLanguageId(language);
        topicRepository.findById(topicId).orElseThrow(() -> new TopicNotFoundException());

        List<Notion> notions = notionRepository.findByTopic_IdOrderByPageNoAsc(topicId);
        if (notions.isEmpty()) { throw new NotionTopicNotExistsException(); }

        List<Long> notionIds = notions.stream().map(Notion::getId).toList();
        List<NotionCode> languageCodes = notionCodeRepository.findByNotion_IdInAndLanguage_Id(notionIds, languageId);
        List<NotionCode> allCodes = notionCodeRepository.findByNotion_IdIn(notionIds);
        List<NotionCompletion> completions = notionCompletionRepository.findByNotion_IdInAndUser_Id(notionIds, userId);

        Map<Long, NotionCode> languageCodeMap = languageCodes.stream()
                .collect(Collectors.toMap(
                        code -> code.getNotion().getId(),
                        Function.identity()));

        Set<Long> notionWithAnyCodeIds = allCodes.stream()
                .map(code -> code.getNotion().getId())
                .collect(Collectors.toSet());

        Set<Long> completedNotionIds = completions.stream()
                .map(completion -> completion.getNotion().getId())
                .collect(Collectors.toSet());

        List<LearningNotion> lists = notions.stream()
                .map(notion -> {
                    Long notionId = notion.getId();

                    NotionCode notionCode = languageCodeMap.get(notionId);
                    boolean hasAnyCode = notionWithAnyCodeIds.contains(notionId);
                    boolean completed = completedNotionIds.contains(notionId);

                    return LearningNotionConverter.toNotionLResponse(notion, notionCode, language, completed, hasAnyCode);
                }).toList();

        return LearningNotionResponse.builder()
                .topicId(topicId)
                .count(lists.size())
                .notions(lists)
                .build();
    }

    @Override
    public LearningNotionCompletionResponse notionComplete(Long notionId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Notion notion = notionRepository.findById(notionId).orElseThrow(NotionNotExistsException::new);

        NotionCompletion completion = NotionCompletion.builder()
                .user(user)
                .notion(notion)
                .build();

        notionCompletionRepository.save(completion);

        return LearningNotionCompletionResponse.builder()
                .notionId(notionId)
                .userName(user.getNickname())
                .notionCompleted(true)
                .build();
    }
}