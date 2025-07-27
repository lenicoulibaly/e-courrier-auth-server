package lenicorp.security.controller.services.specs;

import jakarta.transaction.Transactional;
import lenicorp.security.model.dtos.AuthResponse;
import lenicorp.security.model.dtos.CreateUserDTO;
import lenicorp.security.model.dtos.UserDTO;
import lenicorp.security.model.entities.AuthToken;
import lenicorp.utilities.Page;
import lenicorp.utilities.PageRequest;

import java.util.List;

public interface IUserService
{
    UserDTO createUser(UserDTO user);

    UserDTO createUserWithProfile(CreateUserDTO user);

    UserDTO updateUser(UserDTO user);

    void changePassword(UserDTO user);

    void resetPassword(UserDTO user);

    void blockUser(Long userId);

    void unblockUser(Long userId);

    void sendResetPasswordEmail(Long userId);

    void sendResetPasswordEmail(String email);

    void sendActivationEmail(Long userId);

    void activateAccount(UserDTO user);

    Page<UserDTO> searchUsers(String key, Long strId, PageRequest pageRequest);

    List<UserDTO> getVisibleUsers();

    UserDTO findByUsername(String username);

    AuthToken generateAuthToken(Long userId);
    void invalidateAuthToken(String token);

    @Transactional
    AuthResponse login(UserDTO dto);

    @Transactional
    AuthResponse refreshToken(Long userId);
}
