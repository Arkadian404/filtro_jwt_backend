package com.ark.security.repository.user;

import com.ark.security.models.user.Role;
import com.ark.security.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findUserByUsername(String username);
    Optional<User> findByEmail(String email);

    List<User> findUserByRole(Role role);
    boolean existsUserByUsername(String username);
    boolean existsUserByEmail(String email);
}
