package lenicorp.security.controller.services.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lenicorp.exceptions.AppException;
import lenicorp.security.controller.repositories.impl.UserRepo;
import lenicorp.security.controller.repositories.spec.IAuthAssoRepo;
import lenicorp.security.controller.repositories.spec.IAuthorityRepo;
import lenicorp.security.controller.repositories.spec.IVProfileRepo;
import lenicorp.security.controller.services.specs.IAuthorityService;
import lenicorp.security.controller.services.specs.IJwtService;
import lenicorp.security.model.dtos.AuthResponse;
import lenicorp.security.model.dtos.AuthorityDTO;
import lenicorp.security.model.dtos.UserProfileAssoDTO;
import lenicorp.security.model.entities.AppAuthority;
import lenicorp.security.model.entities.AuthAssociation;
import lenicorp.security.model.mappers.AuthAssoMapper;
import lenicorp.security.model.mappers.AuthorityMapper;
import lenicorp.security.model.views.VProfile;
import lenicorp.types.model.entities.Type;
import lenicorp.utilities.Page;
import lenicorp.utilities.PageRequest;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Set;

@ApplicationScoped
@RequiredArgsConstructor
public class AuthorityService implements IAuthorityService
{
    private final AuthorityMapper authMapper;
    private final AuthAssoMapper authAssoMapper;
    private final IAuthorityRepo authorityRepo;
    private final IAuthAssoRepo authAssoRepo;
    private final IVProfileRepo vProfileRepo;
    private final IJwtService jwtService;
    private final UserRepo userRepo;

    @Override
    public Set<String> getAuthoritiesByUsername(String username)
    {
        return authAssoRepo.findAuthoritiesByUsername(username);
    }

    @Override @Transactional
    public AuthorityDTO createPrivilege(AuthorityDTO dto)
    {
        AppAuthority authority = authMapper.mapToPrivilege(dto);
        authorityRepo.persist(authority);
        return authMapper.mapToAuthorityDTO(authority);
    }

    @Transactional
    @Override
    public void createPrivilege(List<AuthorityDTO> dtos)
    {
        if(dtos == null || dtos.isEmpty()) return ;
        dtos.forEach(dto->this.createPrivilege(dto));
    }

    @Override @Transactional
    public AuthorityDTO updatePrivilege(AuthorityDTO dto)
    {
        AppAuthority authority = authorityRepo.findById(dto.getPrivilegeCode());
        if(authority == null) throw new AppException("Le privilege n'existe pas");
        authority = authMapper.partialUpdate(dto, authority);
        authorityRepo.persist(authority);
        AuthorityDTO authorityDTO = authMapper.mapToAuthorityDTO(authority);
        authorityDTO.setPrivilegeCode(dto.getPrivilegeCode());
        return authorityDTO;
    }

    @Override @Transactional
    public AuthorityDTO createRole(AuthorityDTO dto)
    {
        AppAuthority authority = authMapper.mapToRole(dto);
        authorityRepo.persist(authority);
        List<AuthorityDTO> privilegeDtos = dto.getChildren();
        if( privilegeDtos != null )
        {
            privilegeDtos.forEach( privilegeDto ->
            {
                addPrivilegeToRole(dto.getCode(), privilegeDto.getCode());
            });
        }
        return dto;
    }

    @Override @Transactional
    public AuthorityDTO updateRole(AuthorityDTO dto)
    {
        String roleCode = dto.getRoleCode();
        List<AuthorityDTO> privilegeDtos = dto.getChildren();
        AppAuthority authority = authorityRepo.findById(roleCode);
        if(authority == null) throw new AppException("Le role n'existe pas");
        authority = authMapper.partialUpdate(dto, authority);
        authorityRepo.persist(authority);
        AuthorityDTO authorityDTO = authMapper.mapToAuthorityDTO(authority);
        if(privilegeDtos == null || privilegeDtos.isEmpty()) return authorityDTO;
        List<String> privilegeCodes = privilegeDtos.stream().map(AuthorityDTO::getCode).toList();
        List<AuthorityDTO> privilegesToAdd = authAssoRepo.findPrivilesToAddOnRole(roleCode, privilegeCodes);
        List<AuthorityDTO> privilegesToRemove = authAssoRepo.findPrivilesToRemoveOnRole(roleCode, privilegeCodes);

        privilegesToAdd.forEach( privilegeDto ->addPrivilegeToRole(roleCode, privilegeDto.getCode()));
        privilegesToRemove.forEach( privilegeDto ->authAssoRepo.removePrivilegeToRole(roleCode, privilegeDto.getCode()));
        authorityDTO.setChildren(privilegesToAdd);
        authorityDTO.setRoleCode( roleCode);
        return authorityDTO;
    }

    @Override @Transactional
    public AuthorityDTO createProfile(AuthorityDTO dto)
    {
        AppAuthority authority = authMapper.mapToProfile(dto);
        authorityRepo.persist(authority);
        List<AuthorityDTO> roleDtos = dto.getChildren();
        if( roleDtos != null )
        {
            roleDtos.forEach( roleDto ->
            {
                addRoleToProfile(dto.getCode(), roleDto.getCode());
            });
        }
        return dto;
    }

    @Override @Transactional
    public AuthorityDTO updateProfile(AuthorityDTO dto)
    {
        String profileCode = dto.getProfileCode();
        List<AuthorityDTO> roleDtos = dto.getChildren();
        AppAuthority authority = authorityRepo.findById(profileCode);
        if(authority == null) throw new AppException("Le profile n'existe pas");
        authority = authMapper.partialUpdate(dto, authority);
        authorityRepo.persist(authority);
        AuthorityDTO authorityDTO = authMapper.mapToAuthorityDTO(authority);
        if(roleDtos == null || roleDtos.isEmpty()) return authorityDTO;
        List<String> roleCodes = roleDtos.stream().map(AuthorityDTO::getCode).toList();
        List<AuthorityDTO> rolesToAdd = authAssoRepo.findRolesToAddOnProfile(profileCode, roleCodes);
        List<AuthorityDTO> rolesToRemove = authAssoRepo.findRolesToRemoveOnProfile(profileCode, roleCodes);
        rolesToAdd.forEach( roleDto ->addRoleToProfile(profileCode, roleDto.getCode()));
        rolesToRemove.forEach( roleDto ->authAssoRepo.removeRoleToProfile(profileCode, roleDto.getCode()));
        authorityDTO.setChildren(rolesToAdd);
        authorityDTO.setCode( profileCode);
        return authorityDTO;
    }

    @Override
    @Transactional
    public void addProfileToUser(UserProfileAssoDTO dto)
    {
        AuthAssociation association = authAssoMapper.toEntity(dto);
        authAssoRepo.persist(association);
        if(!authAssoRepo.userHasAnyProfile(dto.getUserId()))association.setAssStatus(new Type("STA_ASS_CUR"));
        else association.setAssStatus(new Type("STA_ASS_ACT"));
        authAssoRepo.persist(association);
    }

    @Override
    public Page<AuthorityDTO> searchPrivileges(String key, List<String> privilegeTypeCodes, PageRequest pageRequest)
    {
        return authAssoRepo.searchPrivileges(key, privilegeTypeCodes, pageRequest);
    }

    @Override
    public List<AuthorityDTO> searchPrivilegesByRoleCode(String roleCode, String key, List<String> privilegeTypeCodes)
    {
        return authAssoRepo.searchPrivilegesByRoleCode(roleCode, key, privilegeTypeCodes);
    }

    @Override
    public List<AuthorityDTO> searchPrivilegesByProfileleCode(String profileCode, String key, List<String> privilegeTypeCodes)
    {
        return authAssoRepo.searchPrivilegesByProfileCode(profileCode, key, privilegeTypeCodes);
    }

    @Override
    public Page<AuthorityDTO> searchRoles(String key, PageRequest pageRequest)
    {
        return authAssoRepo.searchRoles(key, pageRequest);
    }

    @Override
    public List<AuthorityDTO> searchRolesByProfileCode(String profileCode, String key)
    {
        return authAssoRepo.searchRolesByProfileCode(profileCode, key);
    }

    @Override
    public Page<AuthorityDTO> searchProfiles(String key, PageRequest pageRequest)
    {
        return authAssoRepo.searchProfiles(key, pageRequest);
    }

    @Override
    public Page<AuthorityDTO> searchProfilesByUserId(Long userId, String key, PageRequest pageRequest)
    {
        return authAssoRepo.searchProfilesByUserId(userId, key, pageRequest);
    }

    @Override
    public List<AuthorityDTO> getPrivilegesListByTypeCode(List<String> privilegeTypeCodes)
    {
        return authAssoRepo.getPrivilegesListByTypeCode(privilegeTypeCodes);
    }

    @Override
    public List<AuthorityDTO> getPrivilegesListByRoleCodes(List<String> roleCodes)
    {
        return authAssoRepo.getPrivilegesListByRoleCodes(roleCodes);
    }

    @Override
    public List<VProfile> getAllProfiles()
    {
        return vProfileRepo.findAll().list();
    }

    @Override
    public Page<UserProfileAssoDTO> searchUserProfileAssignations(Long userId, Long strId, String profileCode, String key, PageRequest pageRequest)
    {
        Long currentProfileStrId = jwtService.getCurrentUserProfileStrId();
        if(strId == null) strId = currentProfileStrId;
        return authAssoRepo.searchUserProfileAssignments(userId, strId, profileCode, key, pageRequest);
    }

    @Override
    public List<UserProfileAssoDTO> findActiveAndCurrentProfilesByUserId(Long userId)
    {
        if(userId == null) throw new AppException("L'identifiant de l'utilisateur est obligatoire");
        return authAssoRepo.findActiveAndCurrentProfilesByUserId(userId);
    }

    @Override
    @Transactional
    public UserProfileAssoDTO updateUserProfileAssignment(UserProfileAssoDTO dto)
    {
        // Find the existing association
        AuthAssociation association = authAssoRepo.findById(dto.getId());
        if (association == null) {
            throw new AppException("L'association avec ID " + dto.getId() + " n'existe pas");
        }

        // Verify that the association has type.code="USR_PRFL"
        if (association.getType() == null || !"USR_PRFL".equals(association.getType().code)) {
            throw new AppException("L'association avec ID " + dto.getId() + " n'est pas de type USR_PRFL");
        }

        // Update the entity with values from the DTO
        association = authAssoMapper.partialUpdate(dto, association);

        // Persist the updated entity
        authAssoRepo.persist(association);

        // Return the updated DTO
        return authAssoMapper.toDto(association);
    }

    private void addPrivilegeToRole(String roleCode, String privilegeCode)
    {
        if( roleCode == null || privilegeCode == null ) return;
        if(!authorityRepo.existsByCodeAndType(roleCode, "ROL")) throw new AppException(String.format("Le code de role %s n'existe pas", roleCode));
        if(!authorityRepo.existsByCodeAndType(privilegeCode, "PRV")) throw new AppException(String.format("Le code de privilege %s n'existe pas", privilegeCode));
        if(authAssoRepo.existsByRoleCodeAndPrivilegeCode(roleCode, privilegeCode)) return;
        AuthAssociation association = AuthAssociation.createRolPrvAss(roleCode, privilegeCode);
        authAssoRepo.persist(association);
    }

    private void addRoleToProfile(String profileCode, String roleCode)
    {
        if( roleCode == null || profileCode == null ) return;
        if(!authorityRepo.existsByCodeAndType(profileCode, "PRFL")) throw new AppException(String.format("Le code de profile %s n'existe pas", profileCode));
        if(!authorityRepo.existsByCodeAndType(roleCode, "ROL")) throw new AppException(String.format("Le code de role %s n'existe pas", roleCode));

        if(authAssoRepo.existsByProfileCodeAndRoleCode(profileCode, roleCode)) return;
        AuthAssociation association = AuthAssociation.createPrflRolAss(profileCode, roleCode);
        authAssoRepo.persist(association);
    }

    @Override
    @Transactional
    public void revokeProfileAssignment(Long id) {
        // Find the existing association
        AuthAssociation association = authAssoRepo.findById(id);
        if (association == null) {
            throw new AppException("L'association avec ID " + id + " n'existe pas");
        }

        // Verify that the association has type.code="USR_PRFL"
        if (association.getType() == null || !"USR_PRFL".equals(association.getType().code)) {
            throw new AppException("L'association avec ID " + id + " n'est pas de type USR_PRFL");
        }

        // Set the status to inactive
        association.setAssStatus(new Type("STA_ASS_INACT"));

        // Persist the updated entity
        authAssoRepo.persist(association);
    }

    @Override
    @Transactional
    public void restoreProfileAssignment(Long id) {
        // Find the existing association
        AuthAssociation association = authAssoRepo.findById(id);
        if (association == null) {
            throw new AppException("L'association avec ID " + id + " n'existe pas");
        }

        // Verify that the association has type.code="USR_PRFL"
        if (association.getType() == null || !"USR_PRFL".equals(association.getType().code)) {
            throw new AppException("L'association avec ID " + id + " n'est pas de type USR_PRFL");
        }

        // Verify that the association is currently inactive
        if (association.getAssStatus() == null || !"STA_ASS_INACT".equals(association.getAssStatus().code)) {
            throw new AppException("L'association avec ID " + id + " n'est pas dans un état inactif");
        }

        // Set the status to active
        association.setAssStatus(new Type("STA_ASS_ACT"));

        // Persist the updated entity
        authAssoRepo.persist(association);
    }

    @Override
    @Transactional
    public AuthResponse changeDefaultProfile(Long id)
    {
        // Find the association to set as default
        AuthAssociation newDefaultAssociation = authAssoRepo.findById(id);
        if (newDefaultAssociation == null) {
            throw new AppException("L'association avec ID " + id + " n'existe pas");
        }

        // Verify that the association has type.code="USR_PRFL"
        if (newDefaultAssociation.getType() == null || !"USR_PRFL".equals(newDefaultAssociation.getType().code)) {
            throw new AppException("L'association avec ID " + id + " n'est pas de type USR_PRFL");
        }

        // Get the user ID from the association
        Long userId = newDefaultAssociation.getUser().getUserId();

        // Find the current default association for this user
        // We need to use a native query to find the association with status STA_ASS_CUR for this user
        List<AuthAssociation> currentDefaultAssociations = authAssoRepo.find(
            "user.userId = ?1 AND assStatus.code = 'STA_ASS_CUR'", 
            userId
        ).list();

        // Update the current default association to active
        for (AuthAssociation currentDefault : currentDefaultAssociations) {
            currentDefault.setAssStatus(new Type("STA_ASS_ACT"));
            authAssoRepo.persist(currentDefault);
        }

        // Set the new association as default
        newDefaultAssociation.setAssStatus(new Type("STA_ASS_CUR"));
        authAssoRepo.persist(newDefaultAssociation);
        return jwtService.getTokens(userRepo.findById(userId));
    }
}
