package com.pocketco.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Builder @NoArgsConstructor @AllArgsConstructor
@Table(name = "users")
public class User { // 💡 에러 방지를 위해 일단 BaseEntity 상속을 뺐습니다.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id") // 💡 컬럼 이름을 명시해 주면 더 안전해요!
    private Long id;

    private String email;
    private String nickname;
    private String firebaseUid;
}
