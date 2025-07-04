package lenicorp.security.controller.repositories.impl;

import jakarta.enterprise.context.ApplicationScoped;
import lenicorp.security.controller.repositories.spec.IAuthTokenRepo;
import lenicorp.security.model.entities.AuthToken;

import java.time.LocalDateTime;

@ApplicationScoped
public class AuthTokenRepo implements IAuthTokenRepo
{
    public AuthToken findByToken(String token)
    {
        return find("token", token).firstResult();
    }

    public boolean existsByTokenAndUserId(String token, Long userId)
    {
        return count("SELECT COUNT(t) FROM AuthToken t WHERE t.token = ?1 AND t.user.userId = ?2", token, userId) > 0;
    }

    public boolean tokenHasNotExpired(String token)
    {
        AuthToken authToken = findByToken(token);
        if (authToken == null) return false;
        return authToken.getExpirationDate() != null && authToken.getExpirationDate().isAfter(LocalDateTime.now());
    }

    public boolean tokenIsAlreadyUsed(String token)
    {
        return count("SELECT COUNT(t) FROM AuthToken t WHERE t.token = ?1 AND t.alreadyUsed = true", token) > 0;
    }
}