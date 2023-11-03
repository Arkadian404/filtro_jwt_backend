package com.ark.security.service.user;

import com.ark.security.models.token.Token;
import com.ark.security.repository.user.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;

    public List<Token> getAllValidTokenByUserId(Integer userId){
        return tokenRepository.findAllValidTokenByUser(userId);
    }

    public Optional<Token> getByToken(String token){
        return tokenRepository.findByToken(token);
    }

    public void save(Token token){
        tokenRepository.save(token);
    }

    public void saveAll(List<Token> tokens){
        tokenRepository.saveAll(tokens);
    }


}
