package lenicorp.association.controller.resources;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lenicorp.association.controller.services.IAdhesionService;
import lenicorp.association.model.dtos.AdhesionDTO;
import lenicorp.association.model.entities.Adhesion;
import lenicorp.utilities.Page;
import lenicorp.utilities.PageRequest;
import lenicorp.utilities.SelectOption;

import java.util.List;

@Path("/adhesions")
public class AdhesionController
{
    @Inject
    IAdhesionService adhesionService;

    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Adhesion createAdhesion(@Valid AdhesionDTO dto)
    {
        return adhesionService.createUserAndAdhesion(dto);
    }

    @PUT
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Adhesion updateAdhesion(@Valid AdhesionDTO dto)
    {
        return adhesionService.updateMembre(dto);
    }

    @PUT
    @Path("/desabonner/{adhesionId}")
    @Produces(MediaType.APPLICATION_JSON)
    public void desister(@PathParam("adhesionId") Long adhesionId)
    {
        adhesionService.seDesabonner(adhesionId);
    }

    @GET
    @Path("/search-members")
    @Produces(MediaType.APPLICATION_JSON)
    public Page<AdhesionDTO> searchMembers(@QueryParam("key") @DefaultValue("") String key,
                                      @QueryParam("assoId") Long assoId,
                                      @QueryParam("sectionId") Long sectionId,
                                      @QueryParam("page") @DefaultValue("0") int page,
                                      @QueryParam("size") @DefaultValue("10") int size)
    {
        return adhesionService.searchAdhsions(key, assoId, sectionId, PageRequest.of(page, size));
    }

    @GET
    @Path("/all-options")
    @Produces(MediaType.APPLICATION_JSON)
    public List<SelectOption> getAdhesionOptions(@QueryParam("assoId") Long assoId)
    {
        return adhesionService.getOptions(assoId);
    }

    @GET
    @Path("/get-membre-dto")
    @Produces(MediaType.APPLICATION_JSON)
    public AdhesionDTO getMembreDto(@QueryParam("identifier") String identifier)
    {
        return adhesionService.getMembreDTO(identifier);
    }
}
