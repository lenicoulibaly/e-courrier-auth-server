package lenicorp.security.controller.services.impl;

import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lenicorp.exceptions.AppException;
import lenicorp.notification.controller.services.MailServiceInterface;
import lenicorp.security.controller.repositories.spec.IAuthTokenRepo;
import lenicorp.security.controller.repositories.spec.IUserRepo;
import lenicorp.security.controller.services.specs.IJwtService;
import lenicorp.security.controller.services.specs.IUserService;
import lenicorp.security.model.dtos.AuthResponse;
import lenicorp.security.model.dtos.UserDTO;
import lenicorp.security.model.entities.AppUser;
import lenicorp.security.model.entities.AuthToken;
import lenicorp.security.model.mappers.UserMapper;
import lenicorp.utilities.Page;
import lenicorp.utilities.PageRequest;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@ApplicationScoped @RequiredArgsConstructor
public class UserService implements IUserService
{
    private final MailServiceInterface mailService;
    private final IUserRepo userRepo;
    private final UserMapper userMapper;
    private final IAuthTokenRepo authTokenRepo;
    private final IJwtService jwtService;


    @Override @Transactional
    public UserDTO createUser(UserDTO dto)
    {
        AppUser user = userMapper.mapToAppUser(dto);
        userRepo.persist(user);
        sendActivationEmail(user.getUserId());
        return userMapper.mapToUserDTO(user);
    }

    @Override @Transactional
    public UserDTO updateUser(UserDTO dto)
    {
        AppUser user = userRepo.findById(dto.getUserId());
        user = userMapper.updateUser(dto, user);
        userRepo.persist(user);
        return userMapper.mapToUserDTO(user);
    }

    @Override @Transactional
    public void changePassword(UserDTO dto)
    {
        AppUser user = userRepo.findById(dto.getUserId());
        String oldPassword = user.getPassword();
        if (!BcryptUtil.matches(dto.getOldPassword(), oldPassword)) throw new AppException("L'ancien mot de passe est incorrect");
        if (BcryptUtil.matches(dto.getPassword(), oldPassword)) throw new AppException("Le nouveau mot de passe doit être différent de l'ancien");
        user.setPassword(BcryptUtil.bcryptHash(dto.getPassword(), 12));
        userRepo.persist(user);
    }

    @Override @Transactional
    public void resetPassword(UserDTO dto)
    {
        AppUser user = userRepo.findById(dto.getUserId());
        user.setPassword(BcryptUtil.bcryptHash(dto.getPassword(), 12));
        userRepo.persist(user);
        invalidateAuthToken(dto.getAuthToken());
    }

    @Override @Transactional
    public void blockUser(Long userId)
    {
        AppUser user = findUserById(userId);
        if (user == null) return;
        if(!user.isNotBlocked()) return;
        user.setNotBlocked(false);
        userRepo.persist(user);
    }

    private AppUser findUserById(Long userId)
    {
        if(userId == null) return null;
        AppUser user = userRepo.findById(userId);
        if(user == null) throw new AppException("L'utilisateur n'existe pas");
        return user;
    }

    @Override @Transactional
    public void unblockUser(Long userId)
    {
        AppUser user = findUserById(userId);
        if (user == null) return;
        if(user.isNotBlocked()) return;
        user.setNotBlocked(true);
        userRepo.persist(user);
    }

    @Override @Transactional
    public void sendResetPasswordEmail(Long userId)
    {
        AuthToken authToken = this.generateAuthToken(userId);
        AppUser user = userRepo.findById(userId);
        if(user == null) return;
        mailService.envoyerEmailReinitialisation(user.getEmail(), user.getLastName(), "/reset-password?token=" + authToken.getToken()).exceptionally(throwable ->
        {
            throwable.printStackTrace();
            throw new AppException(throwable.getMessage());
        });
        authToken.setEmailSent(true);
    }

    @Override @Transactional
    public void sendActivationEmail(Long userId)
    {
        AuthToken authToken = this.generateAuthToken(userId);
        AppUser user = userRepo.findById(userId);
        if(user == null) return;
        mailService.envoyerEmailActivation(user.getEmail(), user.getLastName(), "/activate-account?token=" + authToken.getToken()).exceptionally(throwable ->
        {
            throwable.printStackTrace();
            throw new AppException(throwable.getMessage());
        });
        authToken.setEmailSent(true);
    }

    @Override @Transactional
    public void activateAccount(UserDTO dto)
    {
        AppUser user = userRepo.findById(dto.getUserId());
        user.setActivated(true);
        user.setNotBlocked(true);
        user.setPassword(BcryptUtil.bcryptHash(dto.getPassword(), 12));
        userRepo.persist(user);
        invalidateAuthToken(dto.getAuthToken());
    }

    @Override @Transactional
    public Page<UserDTO> searchUsers(String key, Long strId, PageRequest pageRequest)
    {
        return userRepo.searchUsers(key, strId, pageRequest);
    }

    @Override
    public UserDTO findByUsername(String username)
    {
        AppUser user = userRepo.findByUsername(username);
        return userMapper.mapToUserDTO(user);
    }

    @Override
    public AuthToken generateAuthToken(Long userId)
    {
        AuthToken authToken = new AuthToken();
        authToken.setToken(UUID.randomUUID().toString());
        authToken.setUser(new AppUser(userId));
        authToken.setExpirationDate(LocalDateTime.now().plusDays(1));
        authToken.setAlreadyUsed(false);
        authToken.setEmailSent(false);
        authTokenRepo.persist(authToken);
        return authToken;
    }

    @Override
    public void invalidateAuthToken(String token)
    {
        AuthToken authToken = authTokenRepo.findByToken(token);
        authToken.setAlreadyUsed(true);
        authToken.setUsageDate(LocalDateTime.now());
        authTokenRepo.persist(authToken);
    }

    @Transactional @Override
    public AuthResponse login(UserDTO dto)
    {
        AppUser user = userRepo.findByUsername(dto.getEmail());
        if(user == null) throw new AppException("nom d'utilisateur ou mot de passe incorrect");

        user.setLastLogin(LocalDateTime.now());
        userRepo.persist(user);
        return jwtService.getTokens(user);
    }

    @Transactional @Override
    public AuthResponse refreshToken(Long userId)
    {
        AppUser user = userRepo.findById(userId);
        if (user == null) throw new AppException("UserId incorrect ou inexistant : " + userId);

        user.setLastLogin(LocalDateTime.now());
        userRepo.persist(user);
        return jwtService.getTokens(user);
    }
}
