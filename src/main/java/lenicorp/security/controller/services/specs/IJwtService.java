package lenicorp.security.controller.services.specs;

import lenicorp.security.model.dtos.AuthResponse;
import lenicorp.security.model.entities.AppUser;

public interface IJwtService
{
    String generateAccessToken(AppUser user);
    String generateRefreshToken(AppUser user);

    AuthResponse getTokens(AppUser user);
}
