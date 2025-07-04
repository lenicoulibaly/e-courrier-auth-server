package lenicorp.security.controller.repositories.spec;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import lenicorp.security.model.dtos.UserDTO;
import lenicorp.security.model.entities.AppUser;
import lenicorp.utilities.Page;
import lenicorp.utilities.PageRequest;

public interface IUserRepo extends PanacheRepository<AppUser>
{
    AppUser findByUsername(String username);
    boolean existsById(Long userId);
    boolean existsByEmail(String email);
    boolean existsByEmail(String email, Long userId);
    Page<UserDTO> searchUsers(String key, Long strId, PageRequest pageRequest);
    boolean existsByTel(String tel, Long userId);
    boolean existsByTel(String tel);
    boolean userIsActive(Long userId);
    boolean userIsActive(String username);
    boolean userIsNotBlocked(Long userId);
    boolean userIsNotBlocked(String username);
    String getPasswordByUsername(String username);
}