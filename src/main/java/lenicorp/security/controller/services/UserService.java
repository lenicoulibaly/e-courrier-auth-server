package lenicorp.security.controller.services;

import jakarta.enterprise.context.ApplicationScoped;
import lenicorp.exceptions.AppException;
import lenicorp.notification.controller.services.MailServiceInterface;
import lenicorp.notification.model.dto.MailRequest;
import lenicorp.security.controller.repositories.UserRepo;
import lenicorp.security.model.dtos.UserDTO;
import lenicorp.security.model.entities.AppUser;
import lenicorp.utilities.Page;
import lenicorp.utilities.PageRequest;
import lombok.RequiredArgsConstructor;

@ApplicationScoped @RequiredArgsConstructor
public class UserService implements IUserService
{
    private final MailServiceInterface mailService;
    private final UserRepo userRepo;
    @Override
    public UserDTO createUser(UserDTO user)
    {
        return null;
    }

    @Override
    public UserDTO updateUser(UserDTO user)
    {
        return null;
    }

    @Override
    public void changePassword(UserDTO user)
    {

    }

    @Override
    public void resetPassword(UserDTO user)
    {

    }

    @Override
    public void blockUser(Long userId)
    {

    }

    @Override
    public void unblockUser(Long userId)
    {

    }

    @Override
    public void sendResetPasswordEmail(Long userId)
    {
        //TODO Générer un AuthToken pour le lient
        AppUser user = userRepo.findById(userId);
        if(user == null) return;
        mailService.envoyerEmailReinitialisation(user.getEmail(), user.getLastName(), "/reset-password").exceptionally(throwable ->
        {
            throwable.printStackTrace();
            throw new AppException(throwable.getMessage());
        });
    }

    @Override
    public void sendActivationEmail(Long userId)
    {
        //TODO Générer un AuthToken pour le lien
        AppUser user = userRepo.findById(userId);
        if(user == null) return;
        mailService.envoyerEmailActivation(user.getEmail(), user.getLastName(), "/activate-account").exceptionally(throwable ->
        {
            throwable.printStackTrace();
            throw new AppException(throwable.getMessage());
        });
    }

    @Override
    public Page<UserDTO> searchUsers(String key, Long strId, PageRequest pageRequest)
    {
        return null;
    }

    @Override
    public UserDTO getUser(String username)
    {
        return null;
    }
}
