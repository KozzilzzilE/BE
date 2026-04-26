package com.pocketco.domain.language.application;

import com.pocketco.domain.admin.dto.AddLanguageRequest;
import com.pocketco.domain.admin.dto.AddLanguageResponse;
import com.pocketco.domain.language.dto.LanguageList;
import com.pocketco.domain.language.dto.LanguageListResponse;
import com.pocketco.domain.language.exception.AlreadyExistsLanguageException;
import com.pocketco.domain.language.entity.Language;
import com.pocketco.domain.language.exception.LanguageNotExists;
import com.pocketco.domain.language.exception.LanguageNotFoundException;
import com.pocketco.domain.language.repository.LanguageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class LanguagesServiceImpl implements LanguageService {
    private final LanguageRepository languageRepository;

    private void existsLanguage(String name, Integer code) {
        if (languageRepository.existsByName(name) || languageRepository.existsByCode(code)) {
            throw new AlreadyExistsLanguageException();
        }
    }

    @Override
    public Language findLanguageWithName(String name) {
        Language language = languageRepository.findByName(name).orElseThrow(LanguageNotFoundException::new);
        return language;
    }

    @Override
    public Language findLanguageWithId(Long id) {
        Language language = languageRepository.findById(id).orElseThrow(LanguageNotFoundException::new);
        return language;
    }

    @Override
    public AddLanguageResponse addLanguage(AddLanguageRequest req) {
        existsLanguage(req.name(), req.code());

        Language language = Language.builder()
                .name(req.name())
                .code(req.code())
                .build();

        Language saved = languageRepository.save(language);

        return AddLanguageResponse.builder()
                .languagesId(saved.getId())
                .build();
    }

    @Override
    public Map<Long, Language> validateAndGetLanguageMap(List<Long> languageIds) {
        Set<Long> unique = new HashSet<>(languageIds);
        List<Language> languages = languageRepository.findAllById(unique);

        if (unique.size() != languageIds.size() || languages.size() != unique.size()) {
            throw new LanguageNotFoundException();
        }

        return languages.stream()
                .collect(Collectors.toMap(Language::getId, Function.identity()));
    }

    @Override
    public LanguageListResponse getLanguagesList() {
        List<Language> languages = languageRepository.findAllByOrderByIdAsc();

        if (languages.isEmpty()) {
            throw new LanguageNotExists();
        }

        List<LanguageList> list = new ArrayList<>();
        for (Language language : languages) {
            LanguageList item = LanguageList.builder()
                    .languageId(language.getId())
                    .name(language.getName())
                    .build();
            list.add(item);
        }
        return LanguageListResponse.builder()
                .count(languages.size())
                .languages(list)
                .build();
    }

    @Override
    public Long getLanguageId(String languageName) {
        Language language = languageRepository.findByName(languageName).orElseThrow(LanguageNotFoundException::new);
        return language.getId();
    }
}