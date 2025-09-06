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

    /**
     * Retrieves a claim from the current JWT token by its name
     * @param claimName the name of the claim to retrieve
     * @return the value of the claim, or null if the claim doesn't exist
     */
    Object getClaimFromToken(String claimName);

    /**
     * Retrieves the raw JWT token from the current connection
     * @return the raw JWT token, or null if no token is present
     */
    String getCurrentToken();
}
