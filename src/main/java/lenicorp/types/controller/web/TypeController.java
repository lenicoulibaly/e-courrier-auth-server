
package lenicorp.types.controller.web;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.groups.ConvertGroup;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lenicorp.types.controller.services.ITypeService;
import lenicorp.types.model.dtos.TypeDTO;
import lenicorp.types.model.dtos.TypeGroupDTO;
import lenicorp.utilities.Page;
import lenicorp.utilities.PageRequest;
import lenicorp.utilities.validatorgroups.CreateGroup;
import lenicorp.utilities.validatorgroups.SetSousTypeGroup;
import lenicorp.utilities.validatorgroups.UpdateGroup;

import java.util.List;
import java.util.Map;

@Path("/types")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TypeController {

    @Inject
    ITypeService typeService;

    @POST
    public TypeDTO createType(@Valid  @ConvertGroup(to = CreateGroup.class)  TypeDTO dto) {
        return typeService.createType(dto);
    }

    @PUT
    public TypeDTO updateType(@Valid  @ConvertGroup(to = UpdateGroup.class) TypeDTO dto) {
        return typeService.updateType(dto);
    }

    @GET
    @Path("/search")
    public Page<TypeDTO> searchTypes(
            @QueryParam("key") String key,
            @QueryParam("groupCodes") List<String> groupCodes,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size) {
        return typeService.searchTypes(key, groupCodes, PageRequest.of(page, size));
    }

    @POST
    @Path("/groups")
    public TypeGroupDTO createTypeGroup(@Valid @ConvertGroup(to = CreateGroup.class) TypeGroupDTO dto) {
        return typeService.createTypeGroup(dto);
    }

    @PUT
    @Path("/groups")
    public TypeGroupDTO updateTypeGroup(@Valid  @ConvertGroup(to = UpdateGroup.class) TypeGroupDTO dto) {
        return typeService.updateTypeGroup(dto);
    }

    @GET
    @Path("/groups/search")
    public Page<TypeGroupDTO> searchTypeGroups(
            @QueryParam("key") String key,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size) {
        return typeService.searchTypeGroups(key, PageRequest.of(page, size));
    }

    @GET
    @Path("/groups/list")
    public List<TypeGroupDTO> getAllTypeGroups()
    {
        return typeService.getAllTypeGroups();
    }

    @GET
    @Path("/direct-sous-types")
    @Produces(MediaType.APPLICATION_JSON)
    public List<TypeDTO> getDirectSousTypes(@QueryParam("parentCode") String parentCode) {
        return typeService.getDirectSousTypes(parentCode);
    }

    @POST
    @Path("/set-sous-types")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response setSousTypes(@Valid @ConvertGroup(to = SetSousTypeGroup.class) TypeDTO dto)
    {
        typeService.setSousTypes(dto);
        return Response.ok().build();
    }

    @GET
    @Path("/possible-sous-types")
    @Produces(MediaType.APPLICATION_JSON)
    public List<TypeDTO> getPossibleSousTypes(@QueryParam("parentCode") String parentCode) {
        return typeService.getPossibleSousTypes(parentCode);
    }

    @GET
    @Path("/by-group/{groupCode}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<TypeDTO> getTypesByGroupCode(@PathParam("groupCode") String groupCode) {
        return typeService.getTypesByGroupCode(groupCode);
    }
}
