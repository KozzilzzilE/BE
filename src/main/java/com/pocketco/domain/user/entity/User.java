package com.pocketco.domain.user.entity;

import com.pocketco.domain.baseEntity.BaseEntity;
import com.pocketco.domain.language.entity.Language;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false, unique = true)
    private String firebaseUid;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "language_id", nullable = false)
    private Language language;

    public void updateLanguage(Language language) {
        this.language = language;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }
}