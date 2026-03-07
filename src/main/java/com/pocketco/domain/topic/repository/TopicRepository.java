package com.pocketco.domain.topic.repository;

import com.pocketco.domain.topic.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface TopicRepository extends JpaRepository<Topic, Long> {

    Optional<Topic> findById(Long id);
    boolean existsById(Long id);
    boolean existsByName(String name);
    boolean existsByDisplayName(String displayName);

    List<Topic> findAllByOrderByIdAsc();
}