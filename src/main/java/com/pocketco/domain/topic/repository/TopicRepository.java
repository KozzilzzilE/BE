package com.pocketco.domain.topic.repository;

import com.pocketco.domain.topic.entity.Topic;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TopicRepository extends CrudRepository<Topic, Long> {
    Optional<Topic> findById(Long id);
    boolean existsById(Long id);
    boolean existsByName(String name);
    boolean existsByDisplayName(String displayName);
}