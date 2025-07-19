package lenicorp.security.controller.services.specs;

import jakarta.transaction.Transactional;
import lenicorp.security.model.dtos.AuthorityDTO;
import lenicorp.security.model.dtos.UserProfileAssoDTO;
import lenicorp.security.model.views.VProfile;
import lenicorp.utilities.Page;
import lenicorp.utilities.PageRequest;

import java.util.List;
import java.util.Set;

public interface IAuthorityService
{
    Set<String> getAuthoritiesByUsername(String username);

    AuthorityDTO createPrivilege(AuthorityDTO authorityDTO);

    @Transactional
    void createPrivilege(List<AuthorityDTO> dtos);

    AuthorityDTO updatePrivilege(AuthorityDTO authorityDTO);

    AuthorityDTO createRole(AuthorityDTO authorityDTO);

    AuthorityDTO updateRole(AuthorityDTO authorityDTO);

    AuthorityDTO createProfile(AuthorityDTO authorityDTO);

    AuthorityDTO updateProfile(AuthorityDTO authorityDTO);

    @Transactional
    void addProfileToUser(UserProfileAssoDTO dto);

    /**
     * Update a user profile assignment
     * @param dto The DTO containing the updated information
     * @return The updated UserProfileAssoDTO
     */
    @Transactional
    UserProfileAssoDTO updateUserProfileAssignment(UserProfileAssoDTO dto);

    Page<AuthorityDTO> searchPrivileges(String key, List<String> privilegeTypeCodes, PageRequest pageRequest);
    List<AuthorityDTO> searchPrivilegesByRoleCode(String roleCode, String key, List<String> privilegeTypeCodes);
    List<AuthorityDTO> searchPrivilegesByProfileleCode(String roleCode, String key, List<String> privilegeTypeCodes);

    Page<AuthorityDTO> searchRoles(String key, PageRequest pageRequest);
    List<AuthorityDTO> searchRolesByProfileCode(String profileCode, String key);

    Page<AuthorityDTO> searchProfiles(String key, PageRequest pageRequest);
    Page<AuthorityDTO> searchProfilesByUserId(Long userId, String key, PageRequest pageRequest);

    List<AuthorityDTO> getPrivilegesListByTypeCode(List<String> privilegeTypeCodes);

    List<AuthorityDTO> getPrivilegesListByRoleCodes(List<String> roleCodes);

    /**
     * Get all profiles as VProfile entities
     * @return List of all profiles
     */
    List<VProfile> getAllProfiles();

    /**
     * Search for user profile assignments with pagination and multiple criteria
     * @param userId Optional user ID filter
     * @param profileCode Optional profile code filter
     * @param key Search term for name, email, etc.
     * @param pageRequest Pagination parameters
     * @return Page of UserProfileAssoDTO objects
     */
    Page<UserProfileAssoDTO> searchUserProfileAssignations(Long userId, String profileCode, String key, PageRequest pageRequest);
}
