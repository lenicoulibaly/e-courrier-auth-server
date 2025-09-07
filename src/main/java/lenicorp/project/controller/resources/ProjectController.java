package lenicorp.project.controller.resources;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lenicorp.project.controller.services.IProjectService;
import lenicorp.project.model.dtos.CreateProjectDTO;
import lenicorp.project.model.dtos.ReadProjectDTO;
import lenicorp.project.model.dtos.UpdateProjectDTO;
import lenicorp.project.model.entities.Project;
import lenicorp.utilities.Page;
import lenicorp.utilities.PageRequest;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import java.io.IOException;
import java.util.Base64;

@Path("/projects")
public class ProjectController
{
    @Inject
    IProjectService projectService;

    @POST
    @Path("/create")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Project createProject(@Valid @MultipartForm CreateProjectDTO dto) throws IOException
    {
        return projectService.createProject(dto);
    }

    @PUT
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Project updateProject(@Valid UpdateProjectDTO dto)
    {
        return projectService.updateProject(dto);
    }

    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public Page<ReadProjectDTO> searchProjects(
            @QueryParam("key") @DefaultValue("") String key,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size)
    {
        return projectService.searchProjects(key, PageRequest.of(page, size));
    }

    @GET
    @Path("/search/land-sales")
    @Produces(MediaType.APPLICATION_JSON)
    public Page<ReadProjectDTO> searchLandSaleProjects(
            @QueryParam("key") @DefaultValue("") String key,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size)
    {
        return projectService.searchLandSaleProjects(key, PageRequest.of(page, size));
    }

    @GET
    @Path("/search/housing-acquisitions")
    @Produces(MediaType.APPLICATION_JSON)
    public Page<ReadProjectDTO> searchHousingAcquisitionProjects(
            @QueryParam("key") @DefaultValue("") String key,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size)
    {
        return projectService.searchHousingAcquisitionProjects(key, PageRequest.of(page, size));
    }

    @GET
    @Path("/search/repayable-loans")
    @Produces(MediaType.APPLICATION_JSON)
    public Page<ReadProjectDTO> searchRepayableLoanProjects(
            @QueryParam("key") @DefaultValue("") String key,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size)
    {
        return projectService.searchRepayableLoanProjects(key, PageRequest.of(page, size));
    }

    @GET
    @Path("/find-by-id/{projetId}")
    @Produces(MediaType.APPLICATION_JSON)
    public ReadProjectDTO findById(@PathParam("projetId") Long projetId)
    {
        return projectService.findById(projetId);
    }

    @GET
    @Path("/generate-report/{projetId}")
    @Produces(MediaType.TEXT_PLAIN)
    public String generateProjectReport(@PathParam("projetId") Long projetId) throws Exception {
        byte[] bytes = projectService.generateProjectReport(projetId);
        String base64String = Base64.getEncoder().encodeToString(bytes);
        return base64String;
    }
}
