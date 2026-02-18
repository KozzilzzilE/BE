package com.pocketco.domain.user.repository;

import com.pocketco.domain.user.entity.UserGoal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserGoalRepository extends JpaRepository<UserGoal, Long> {
}