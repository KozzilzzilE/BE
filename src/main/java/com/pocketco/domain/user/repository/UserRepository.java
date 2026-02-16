package com.pocketco.domain.user.repository;

import com.pocketco.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // 💡 이 한 줄을 추가해 주세요! 이메일로 유저를 찾는 마법의 주문입니다.
    Optional<User> findByEmail(String email);
}