package lenicorp.security.controller.web;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.validation.groups.ConvertGroup;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lenicorp.security.controller.services.specs.IAuthorityService;
import lenicorp.security.model.dtos.AuthorityDTO;
import lenicorp.security.model.dtos.UserProfileAssoDTO;
import lenicorp.utilities.Page;
import lenicorp.utilities.PageRequest;
import lenicorp.utilities.validatorgroups.*;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Set;

@Path("/authorities")
@RequiredArgsConstructor
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthorityController
{
    private final IAuthorityService authorityService;

    @GET
    @Path("/{username}")
    public Set<String> getAuthoritiesByUsername(@PathParam("username") String username)
    {
        return authorityService.getAuthoritiesByUsername(username);
    }

    @POST
    @Path("/privileges/create")
    public AuthorityDTO createPrivilege(@Valid @ConvertGroup(to = CreatePrvGroup.class) AuthorityDTO authorityDTO)
    {
        return authorityService.createPrivilege(authorityDTO);
    }

    @POST
    @RolesAllowed("ADMIN")
    @Path("/privileges/creates")
    public void createPrivilege(List<AuthorityDTO> dtos)
    {
        authorityService.createPrivilege(dtos);
    }

    @PUT
    @Path("/privileges/update")
    public AuthorityDTO updatePrivilege(@Valid @ConvertGroup(to = UpdatPrvGroup.class)AuthorityDTO authorityDTO)
    {
        return authorityService.updatePrivilege(authorityDTO);
    }

    @POST
    @Path("/roles/create")
    public AuthorityDTO createRole(@Valid @ConvertGroup(to = CreateGroup.class)AuthorityDTO authorityDTO)
    {
        return authorityService.createRole(authorityDTO);
    }

    @PUT
    @Path("/roles/update")
    public AuthorityDTO updateRole(@Valid @ConvertGroup(to = UpdateRolGroup.class)AuthorityDTO authorityDTO)
    {
        return authorityService.updateRole(authorityDTO);
    }

    @POST
    @Path("/profiles/create")
    public AuthorityDTO createProfile(@Valid @ConvertGroup(to = CreateGroup.class)AuthorityDTO authorityDTO)
    {
        return authorityService.createProfile(authorityDTO);
    }

    @PUT
    @Path("/profiles/update")
    public AuthorityDTO updateProfile(@Valid @ConvertGroup(to = UpdatPrflGroup.class)AuthorityDTO authorityDTO)
    {
        return authorityService.updateProfile(authorityDTO);
    }

    @POST
    @Path("/add-profile-to-user")
    public void addProfileToUser(@Valid @ConvertGroup(to = CreateGroup.class)UserProfileAssoDTO dto)
    {
        authorityService.addProfileToUser(dto);
    }

    // Endpoints pour la recherche des privilèges
    @GET
    @Path("/privileges/search")
    public Page<AuthorityDTO> searchPrivileges(
            @QueryParam("key") @DefaultValue("") String key,
            @QueryParam("privilegeTypeCodes") List<String> privilegeTypeCodes,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size)
    {
        return authorityService.searchPrivileges(key, privilegeTypeCodes, new PageRequest(page, size));
    }

    @GET
    @Path("/privileges/list")
    public List<AuthorityDTO> getPrivilegesListByTypeCode(
            @QueryParam("privilegeTypeCodes") List<String> privilegeTypeCodes)
    {
        return authorityService.getPrivilegesListByTypeCode(privilegeTypeCodes);
    }

    @GET
    @Path("/privileges/search/by-role/{roleCode}")
    public List<AuthorityDTO> searchPrivilegesByRoleCode(
            @PathParam("roleCode") String roleCode,
            @QueryParam("key") @DefaultValue("") String key,
            @QueryParam("privilegeTypeCodes") List<String> privilegeTypeCodes)
    {
        return authorityService.searchPrivilegesByRoleCode(roleCode, key, privilegeTypeCodes);
    }

    @GET
    @Path("/privileges/search/by-profile/{profileCode}")
    public List<AuthorityDTO> searchPrivilegesByProfileCode(
            @PathParam("profileCode") String profileCode,
            @QueryParam("key") @DefaultValue("") String key,
            @QueryParam("privilegeTypeCodes") List<String> privilegeTypeCodes)
    {
        return authorityService.searchPrivilegesByProfileleCode(profileCode, key, privilegeTypeCodes);
    }

    // Endpoints pour la recherche des rôles
    @GET
    @Path("/roles/search")
    public Page<AuthorityDTO> searchRoles(
            @QueryParam("key") @DefaultValue("") String key,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size)
    {
        return authorityService.searchRoles(key, new PageRequest(page, size));
    }

    @GET
    @Path("/roles/search/by-profile/{profileCode}")
    public List<AuthorityDTO> searchRolesByProfileCode(
            @PathParam("profileCode") String profileCode,
            @QueryParam("key") @DefaultValue("") String key)
    {
        return authorityService.searchRolesByProfileCode(profileCode, key);
    }

    // Endpoints pour la recherche des profils
    @GET
    @Path("/profiles/search")
    public Page<AuthorityDTO> searchProfiles(
            @QueryParam("key") @DefaultValue("") String key,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size)
    {
        return authorityService.searchProfiles(key, new PageRequest(page, size));
    }

    @GET
    @Path("/profiles/search/by-user/{userId}")
    public Page<AuthorityDTO> searchProfilesByUserId(
            @PathParam("userId") Long userId,
            @QueryParam("key") @DefaultValue("") String key,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size)
    {
        return authorityService.searchProfilesByUserId(userId, key, new PageRequest(page, size));
    }
}