package com.pocketco.domain.user.repository;

import com.pocketco.domain.user.entity.PublicProfileImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublicProfileImageRepository extends JpaRepository<PublicProfileImage, Long> {
}