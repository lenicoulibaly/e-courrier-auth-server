package lenicorp.structures.controller.web;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lenicorp.structures.controller.service.IStrService;
import lenicorp.structures.model.dtos.ChangeAnchorDTO;
import lenicorp.structures.model.dtos.CreateOrUpdateStrDTO;
import lenicorp.structures.model.dtos.ReadStrDTO;
import lenicorp.utilities.Page;
import lenicorp.utilities.PageRequest;

import java.util.List;

@Path("/structures")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class StrController
{
    @Inject
    private IStrService strService;

    @Path("/search")
    @POST
    public Page<ReadStrDTO> searchStrs(@QueryParam("key") String key,
                                       @QueryParam("parentId") Long parentId,
                                       @QueryParam("typeCode") String typeCode,
                                       @QueryParam("page") @DefaultValue("0") int page,
                                       @QueryParam("size") @DefaultValue("10") int size)
    {
        return strService.searchStrs(key, parentId, typeCode, new PageRequest(page, size));
    }

    @POST
    @Path("/create")
    public ReadStrDTO createStr(CreateOrUpdateStrDTO dto)
    {
        return strService.createStr(dto);
    }

    @PUT
    @Path("/update")
    public ReadStrDTO updateStr(CreateOrUpdateStrDTO dto)
    {
        return strService.updateStr(dto);
    }

    @PUT
    @Path("/change-anchor")
    public ReadStrDTO changeAnchor(ChangeAnchorDTO dto)
    {
        return strService.changeAnchor(dto);
    }

    @GET
    @Path("/search")
    public Page<ReadStrDTO> search(@QueryParam("key") String key,
                                   @QueryParam("key") Long parentId,
                                   @QueryParam("typeCode") String typeCode,
                                   @QueryParam("page") @DefaultValue("0") int page,
                                   @QueryParam("size") @DefaultValue("10") int size)
    {
        return strService.searchStrs(key, parentId, typeCode, PageRequest.of(page, size));
    }

    @GET
    @Path("/root-structures")
    public List<ReadStrDTO> getRootStructures()
    {
        return strService.getRootStructures();
    }

    @GET
    @Path("/possible-parents")
    public List<ReadStrDTO> getPossibleParentStructures(@QueryParam("childTypeCode") String childTypeCode)
    {
        return strService.getPossibleParentStructures(childTypeCode);
    }

    @GET
    @Path("/update-dto/{strId}")
    public CreateOrUpdateStrDTO getUpdateDto(@PathParam("strId") Long strId)
    {
        return strService.getUpdateDto(strId);
    }

    @GET
    @Path("/change-anchor-dto/{strId}")
    public ChangeAnchorDTO getChangeAnchorDto(@PathParam("strId") Long strId)
    {
        return strService.getChangeAnchorDto(strId);
    }
} 