package com.nicat.authverifymicroservice.dao.repository;

import com.nicat.authverifymicroservice.dao.entity.Token;
import com.nicat.authverifymicroservice.dao.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    List<Token> findByUserAndIsLoggedOut(User user, Boolean isLoggedOut);

    Optional<Token> findByAccessToken(String accessToken);
}