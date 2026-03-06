package com.pocketco.domain.language.repository;

import com.pocketco.domain.language.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LanguageRepository extends JpaRepository<Language, Long> {
    boolean existsByName(String name);
    boolean existsByCode(Integer code);
    Optional<Language> findByName(String name);
    List<Language> findAllById(Iterable<Long> ids);
    List<Language> findAllByOrderByIdAsc();
}