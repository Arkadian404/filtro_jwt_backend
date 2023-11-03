package com.ark.security.repository.user;

import com.ark.security.models.token.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Integer> {

    @Query("""
        SELECT t from Token t join User u on t.user.id = u.id where u.id =:userId
        and (t.expired = false and t.revoked = false)
    """)
    List<Token> findAllValidTokenByUser(Integer userId);

    Optional<Token> findByToken(String token);

}
