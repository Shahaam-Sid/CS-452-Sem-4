package com.shahaam.equipend.auth;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByUsernameOrEmail(String username, String email);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    List<User> findTop20ByUsernameStartingWithOrFullNameContainingIgnoreCase(String usernamePrefix, String nameFragment);
}
