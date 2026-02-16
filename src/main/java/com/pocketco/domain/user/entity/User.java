package com.pocketco.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Builder @NoArgsConstructor @AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String email;
    private String nickname;
    private String firebaseUid;
}
