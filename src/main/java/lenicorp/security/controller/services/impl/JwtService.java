
package lenicorp.security.controller.services.impl;

import jakarta.enterprise.context.ApplicationScoped;
import io.smallrye.jwt.build.Jwt;
import jakarta.inject.Inject;
import io.quarkus.security.identity.SecurityIdentity;
import lenicorp.exceptions.AppException;
import lenicorp.security.controller.repositories.impl.UserRepo;
import lenicorp.security.controller.repositories.spec.IAuthAssoRepo;
import lenicorp.security.controller.services.specs.IJwtService;
import lenicorp.security.model.dtos.AuthResponse;
import lenicorp.security.model.entities.AppUser;
import lenicorp.security.model.views.VUserProfile;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.time.Instant;
import java.util.Set;

@ApplicationScoped @RequiredArgsConstructor
public class JwtService implements IJwtService
{
    @ConfigProperty(name = "mp.jwt.verify.issuer")
    String issuer;
    @Inject
    UserRepo userRepo;

    @Inject
    SecurityIdentity securityIdentity;

    @ConfigProperty(name = "jwt.access-token-duration", defaultValue = "3600")
    private long accessTokenDuration;

    @ConfigProperty(name = "jwt.refresh-token-duration", defaultValue = "604800")
    private long refreshTokenDuration;

    private final IAuthAssoRepo authAssoRepo;

    @Override
    public String generateAccessToken(AppUser user)
    {
        user = checkUserNullity(user);
        Long userId = user.getUserId();
        String username = user.getEmail();
        Long strId = user.getStructure() != null ? user.getStructure().getStrId() : null;

        Set<String> authorities = authAssoRepo.findAuthoritiesByUsername(username);
        VUserProfile userProfile = authAssoRepo.findUserCurrentProfile(username);
        var jwt = Jwt.issuer(issuer)
                .upn(username)
                .subject(userId.toString())
                .claim("firstName", user.getFirstName())
                .claim("lastName", user.getLastName())
                .claim("userStrId", strId)
                .groups(authorities)
                .expiresAt(Instant.now().plusSeconds(accessTokenDuration * 24*30*12));
        if(userProfile == null) return jwt.sign();
        return jwt.claim("currentProfileCode", userProfile.getProfileCode())
                .claim("currentProfileName", userProfile.getProfileName())
                .claim("currentProfileStrId", userProfile.getUserStrId())
                .claim("profileStrId", userProfile.getProfileStrId())
                .claim("profileStrName", userProfile.getProfileStrName())
                .claim("profileStrSigles", userProfile.getProfileStrSigles())
                .claim("profileStrChaineSigles", userProfile.getProfileStrChaineSigles())
                .sign();
    }

    @Override
    public String generateRefreshToken(AppUser user)
    {
        user = checkUserNullity(user);

        return Jwt.issuer(issuer)
                .upn(user.getEmail())
                .subject(user.getUserId().toString())
                .expiresAt(Instant.now().plusSeconds(refreshTokenDuration))
                .sign();
    }

    private AppUser checkUserNullity(AppUser user)
    {
        if (user == null || (user.getUserId() == null && user.getEmail() == null)) throw new AppException("User must not be null and must have a valid userId");
        if(user.getEmail() == null) user = userRepo.findById(user.getUserId());
        if(user.getUserId() == null) user = userRepo.findByUsername(user.getEmail());
        return user;
    }

    @Override
    public AuthResponse getTokens(AppUser user)
    {
        String accessToken = generateAccessToken(user);
        String refreshToken = generateRefreshToken(user);
        return new AuthResponse(accessToken, refreshToken);
    }

    @Override
    public AppUser getCurrentUser()
    {
        if (securityIdentity != null && !securityIdentity.isAnonymous())
        {
            String username = securityIdentity.getPrincipal().getName();
            return userRepo.findByUsername(username);
        }
        AppUser annonymousUser = new AppUser();
        annonymousUser.setUserId(-1L);
        annonymousUser.setEmail("anonymous");
        annonymousUser.setActivated(true);
        annonymousUser.setNotBlocked(true);
        annonymousUser.setFirstName("anonymous");
        annonymousUser.setLastName("anonymous");
        return annonymousUser;
    }

    @Override
    public VUserProfile getCurrentUserProfile()
    {
        if (securityIdentity != null && !securityIdentity.isAnonymous())
        {
            String username = securityIdentity.getPrincipal().getName();
            return authAssoRepo.findUserCurrentProfile(username);
        }
        return null;
    }

    @Override
    public Long getCurrentUserProfileStrId()
    {
        if (securityIdentity == null || securityIdentity.isAnonymous()) return null;
        VUserProfile userProfile = getCurrentUserProfile();
        return userProfile == null ? null : userProfile.getProfileStrId();
    }
}
