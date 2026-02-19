package com.pocketco.domain.language.application;

import com.pocketco.domain.admin.dto.AddLanguageRequest;
import com.pocketco.domain.admin.dto.AddLanguageResponse;
import com.pocketco.domain.language.exception.AlreadyExistsLanguageException;
import com.pocketco.domain.language.entity.Language;
import com.pocketco.domain.language.exception.LanguageNotFoundException;
import com.pocketco.domain.language.repository.LanguageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Language findLanguageId(String name) {
        Language language = languageRepository.findByName(name).orElseThrow(LanguageNotFoundException::new);
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
}