package lenicorp.structures.controller.web;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lenicorp.structures.controller.service.IStrService;
import lenicorp.structures.model.dtos.ReadStrDTO;
import lenicorp.utilities.Page;
import lenicorp.utilities.PageRequest;

@Path("/structures")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class StrController
{
    @Inject private IStrService strService;

    @Path("/search")
    @POST
    public Page<ReadStrDTO> searchStrs(@QueryParam("key") String key, @QueryParam("parentId") Long parentId, @QueryParam("typeCode") String typeCode, @QueryParam("page") @DefaultValue("0") int page, @QueryParam("size") @DefaultValue("10") int size)
    {
        return strService.searchStrs(key, parentId, typeCode, new PageRequest(page, size));
    }
}
