package com.nicat.authverifymicroservice.dao.repository;

import com.nicat.authverifymicroservice.dao.entity.Authority;
import com.nicat.authverifymicroservice.model.enums.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthRepository extends JpaRepository<Authority,Long> {
    Optional<Authority> findByRoles(Roles role);
}