package lenicorp.security.controller.services;

import lenicorp.security.model.dtos.UserDTO;
import lenicorp.utilities.Page;
import lenicorp.utilities.PageRequest;

public interface IUserService
{
    UserDTO createUser(UserDTO user);

    UserDTO updateUser(UserDTO user);

    void changePassword(UserDTO user);

    void resetPassword(UserDTO user);

    void blockUser(Long userId);

    void unblockUser(Long userId);

    void sendResetPasswordEmail(Long userId);

    void sendActivationEmail(Long userId);

    Page<UserDTO> searchUsers(String key, Long strId, PageRequest pageRequest);

    UserDTO getUser(String username);
}
