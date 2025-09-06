package lenicorp.association.controller.resources;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lenicorp.association.controller.services.ISectionService;
import lenicorp.association.model.dtos.CreateSectionDTO;
import lenicorp.association.model.dtos.ReadSectionDTO;
import lenicorp.association.model.dtos.UpdateSectionDTO;
import lenicorp.utilities.Page;
import lenicorp.utilities.PageRequest;

import java.util.List;

@Path("/sections")
public class SectionController
{
    @Inject
    ISectionService sectionService;

    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ReadSectionDTO createSection(@Valid CreateSectionDTO dto)
    {
        return sectionService.createSection(dto);
    }

    @PUT
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ReadSectionDTO updateSection(@Valid UpdateSectionDTO dto)
    {
        return sectionService.updateSection(dto);
    }

    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public Page<ReadSectionDTO> searchSections(@QueryParam("key") @DefaultValue("") String key,
                                        @QueryParam("assoId") Long assoId,
                                        @QueryParam("strId") Long strId,
                                        @QueryParam("page") @DefaultValue("0") int page,
                                        @QueryParam("size") @DefaultValue("10") int size)
    {
        return sectionService.searchSections(key, assoId, strId, PageRequest.of(page, size));
    }

    @GET
    @Path("/find-by-asso/{assoId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ReadSectionDTO> getAssociationSections(@PathParam("assoId") Long assoId)
    {
        return sectionService.getAssociationSections(assoId);
    }
}
