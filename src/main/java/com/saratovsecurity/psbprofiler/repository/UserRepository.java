package com.saratovsecurity.psbprofiler.repository;

import com.saratovsecurity.psbprofiler.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByActivationCode(String activationCode);
    List<UserEntity> findAll();
    Optional<UserEntity> findByEmail(String email);
    boolean existsByEmail(String email);
}