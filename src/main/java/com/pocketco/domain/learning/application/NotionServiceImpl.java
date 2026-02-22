package com.pocketco.domain.learning.application;

import com.pocketco.domain.admin.dto.AddNotionCodeRequest;
import com.pocketco.domain.admin.dto.AddNotionRequest;
import com.pocketco.domain.admin.dto.AddNotionResponse;
import com.pocketco.domain.language.application.LanguageService;
import com.pocketco.domain.language.entity.Language;
import com.pocketco.domain.admin.converter.AdminConverter;
import com.pocketco.domain.learning.entity.notion.Notion;
import com.pocketco.domain.learning.entity.notion.NotionCode;
import com.pocketco.domain.learning.exception.AlreadyExistsNotionPageException;
import com.pocketco.domain.learning.repository.notion.NotionCodeRepository;
import com.pocketco.domain.learning.repository.notion.NotionCompletionRepository;
import com.pocketco.domain.learning.repository.notion.NotionRepository;
import com.pocketco.domain.topic.entity.Topic;
import com.pocketco.domain.topic.exception.TopicNotFoundException;
import com.pocketco.domain.topic.repository.TopicRepository;
import com.pocketco.global.util.file.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class NotionServiceImpl implements NotionService {
    private final NotionRepository notionRepository;
    private final NotionCodeRepository notionCodeRepository;
    private final NotionCompletionRepository notionCompletionRepository;
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
}