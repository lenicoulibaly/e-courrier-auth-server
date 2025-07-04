
package lenicorp.security.controller.services.impl;

import jakarta.enterprise.context.ApplicationScoped;
import io.smallrye.jwt.build.Jwt;
import lenicorp.exceptions.AppException;
import lenicorp.security.controller.repositories.spec.IAuthAssoRepo;
import lenicorp.security.controller.services.specs.IJwtService;
import lenicorp.security.model.dtos.AuthResponse;
import lenicorp.security.model.entities.AppUser;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.time.Instant;
import java.util.Set;

@ApplicationScoped @RequiredArgsConstructor
public class JwtService implements IJwtService
{
    @ConfigProperty(name = "mp.jwt.verify.issuer")
    String issuer;

    @ConfigProperty(name = "jwt.access-token-duration", defaultValue = "3600")
    private long accessTokenDuration;

    @ConfigProperty(name = "jwt.refresh-token-duration", defaultValue = "604800")
    private long refreshTokenDuration;

    private final IAuthAssoRepo authAssoRepo;

    @Override
    public String generateAccessToken(AppUser user)
    {
        if (user == null || user.getUserId() == null || user.getEmail() == null) throw new AppException("User must not be null and must have a valid userId");
        Long userId = user.getUserId();
        String username = user.getEmail();
        Long strId = user.getStructure() != null ? user.getStructure().getStrId() : null;

        Set<String> authorities = authAssoRepo.findAuthoritiesByUsername(username);

        return Jwt.issuer(issuer)
                .upn(userId.toString())
                .subject(username)
                .claim("firstName", user.getFirstName())
                .claim("lastName", user.getLastName())
                .claim("strId", strId)
                .groups(authorities)
                .expiresAt(Instant.now().plusSeconds(accessTokenDuration * 24*30*12))
                .sign();
    }

    @Override
    public String generateRefreshToken(AppUser user)
    {
        if (user == null || user.getUserId() == null) throw new AppException("User must not be null and must have a valid userId");
        return Jwt.issuer(issuer)
                .upn(user.getEmail())
                .subject(user.getUserId().toString())
                .expiresAt(Instant.now().plusSeconds(refreshTokenDuration))
                .sign();
    }

    @Override
    public AuthResponse getTokens(AppUser user)
    {
        String accessToken = generateAccessToken(user);
        String refreshToken = generateRefreshToken(user);
        return new AuthResponse(accessToken, refreshToken);
    }
}