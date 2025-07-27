package lenicorp.security.controller.services.specs;

import lenicorp.security.model.dtos.AuthResponse;
import lenicorp.security.model.entities.AppUser;
import lenicorp.security.model.views.VUserProfile;

public interface IJwtService
{
    String generateAccessToken(AppUser user);
    String generateRefreshToken(AppUser user);

    AuthResponse getTokens(AppUser user);

    AppUser getCurrentUser();

    VUserProfile getCurrentUserProfile();

    Long getCurrentUserProfileStrId();
}
