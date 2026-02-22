package com.pocketco.domain.language.repository;

import com.pocketco.domain.language.entity.Language;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface LanguageRepository extends CrudRepository<Language, Long> {
    boolean existsByName(String name);
    boolean existsByCode(Integer code);
    Optional<Language> findByName(String name);
    List<Language> findAllById(Iterable<Long> ids);
}