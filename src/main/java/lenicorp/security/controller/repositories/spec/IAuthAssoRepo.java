package lenicorp.security.controller.repositories.spec;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.validation.constraints.NotNull;
import lenicorp.security.model.dtos.AuthorityDTO;
import lenicorp.security.model.entities.AuthAssociation;
import lenicorp.security.model.validators.ExistingUserId;
import lenicorp.utilities.Page;
import lenicorp.utilities.PageRequest;

import java.util.List;
import java.util.Set;

public interface IAuthAssoRepo extends PanacheRepository<AuthAssociation>
{
    AuthorityDTO findUserCurrentProfile(String username);
    List<AuthorityDTO> findRolePrivileges(String roleCode);
    List<AuthorityDTO> findProfileRoles(String profileCode);

    Set<String> findAuthoritiesByRoleCode(String roleCode);
    Set<String> findAuthoritiesByProfileCode(String profileCode);
    Set<String> findAuthoritiesByUsername(String username);

    boolean existsByRoleCodeAndPrivilegeCode(String roleCode, String privilegeCode);

    boolean existsByProfileCodeAndRoleCode(String profileCode, String roleCode);

    List<AuthorityDTO> findPrivilesToAddOnRole(String roleCode, List<String> privilegeCodes);

    List<AuthorityDTO> findPrivilesToRemoveOnRole(String roleCode, List<String> privilegeCodes);

    void removePrivilegeToRole(String roleCode, String code);

    List<AuthorityDTO> findRolesToAddOnProfile(String profileCode, List<String> roleCodes);

    List<AuthorityDTO> findRolesToRemoveOnProfile(String profileCode, List<String> roleCodes);

    void removeRoleToProfile(String profileCode, String code);

    boolean existsByUserIdAndProfileCodeAndStrId(Long userId, String profileCode, Long strId);

    boolean userHasAnyProfile(@ExistingUserId @NotNull(message = "L'utilisateur est obligatoire") Long userId);

    Page<AuthorityDTO> searchPrivileges(String key, List<String> privilegeTypeCodes, PageRequest pageRequest);

    List<AuthorityDTO> getPrivilegesListByTypeCode(List<String> privilegeTypeCodes);
    List<AuthorityDTO> searchPrivilegesByRoleCode(String roleCode, String key, List<String> privilegeTypeCodes);

    List<AuthorityDTO> searchPrivilegesByProfileCode(String profileCode, String key, List<String> privilegeTypeCodes);

    Page<AuthorityDTO> searchRoles(String key, PageRequest pageRequest);

    List<AuthorityDTO> searchRolesByProfileCode(String profileCode, String key);

    Page<AuthorityDTO> searchProfiles(String key, PageRequest pageRequest);

    Page<AuthorityDTO> searchProfilesByUserId(Long userId, String key, PageRequest pageRequest);
}
