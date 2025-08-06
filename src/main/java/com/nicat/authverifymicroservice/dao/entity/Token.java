package com.nicat.authverifymicroservice.dao.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "token")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(length = 1000)
    String accessToken;

    @Column(length = 1000)
    String refreshToken;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;
    Boolean isLoggedOut;

    @PostPersist
    public void setIsLoggedOut() {
        if (this.isLoggedOut == null) {
            this.isLoggedOut = Boolean.FALSE;
        }
    }
}