package lenicorp.association.controller.resources;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lenicorp.association.controller.services.IAssociationService;
import lenicorp.association.model.dtos.CreateAssociationDTO;
import lenicorp.association.model.dtos.ReadAssociationDTO;
import lenicorp.association.model.dtos.UpdateAssociationDTO;
import lenicorp.association.model.entities.Association;
import lenicorp.utilities.Page;
import lenicorp.utilities.PageRequest;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import java.io.IOException;
import java.util.Base64;

@Path("/associations")
public class AssociationController
{
    @Inject
    IAssociationService associationService;

    @POST
    @Path("/create")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Association createAssociation(@Valid @MultipartForm CreateAssociationDTO dto) throws IOException
    {
        return associationService.createAssociation(dto);
    }

    @PUT
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Association updateAssociation(@Valid UpdateAssociationDTO dto)
    {
        return associationService.updateAssociation(dto);
    }

    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public Page<ReadAssociationDTO> searchAssociations(@QueryParam("key") @DefaultValue("") String key,
                                                @QueryParam("strId") Long strId,
                                                @QueryParam("page") @DefaultValue("0") int page,
                                                @QueryParam("size") @DefaultValue("10") int size)
    {
        return associationService.searchAssociations(key, PageRequest.of(page, size));
    }

    @GET
    @Path("/find-by-id/{assoId}")
    @Produces(MediaType.APPLICATION_JSON)
    public ReadAssociationDTO findById(@PathParam("assoId") Long assoId)
    {
        return associationService.findById(assoId);
    }

    @GET
    @Path("/generate-fiche-adhesion/{assoId}")
    @Produces(MediaType.TEXT_PLAIN)
    public String generateFicheAdhesion(@PathParam("assoId") Long assoId) throws Exception {
        byte[] bytes = associationService.generateFicheAdhesion(assoId);
        String base64String = Base64.getEncoder().encodeToString(bytes);
        return base64String;
    }
}
