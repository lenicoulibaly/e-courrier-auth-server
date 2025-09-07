package lenicorp.project.controller.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lenicorp.project.controller.repositories.spec.IProjectRepo;
import lenicorp.project.model.dtos.CreateProjectDTO;
import lenicorp.project.model.dtos.ReadProjectDTO;
import lenicorp.project.model.dtos.UpdateProjectDTO;
import lenicorp.project.model.entities.Project;
import lenicorp.project.model.mappers.ProjectMapper;
import lenicorp.types.model.entities.Type;
import lenicorp.utilities.Page;
import lenicorp.utilities.PageRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class ProjectService implements IProjectService {

    // Constants for project types
    public static final String TYPE_LAND_SALE = "LAND_SALE";
    public static final String TYPE_HOUSING_ACQUISITION = "HOUSING_ACQUISITION";
    public static final String TYPE_REPAYABLE_LOAN = "REPAYABLE_LOAN";

    @Inject
    EntityManager em;

    @Inject
    IProjectRepo projectRepo;

    @Inject
    ProjectMapper projectMapper;

    @Override
    @Transactional
    public Project createProject(CreateProjectDTO dto) throws IOException {
        // Get Type entities from database using the codes
        // The validation is already done by the validators
        Type typeProjet = em.find(Type.class, dto.getTypeProjetCode());
        Type frequencePrelevement = dto.getFrequencePrelevementCode() != null ? em.find(Type.class, dto.getFrequencePrelevementCode()) : null;
        Type statutProjet = dto.getStatutProjetCode() != null ? em.find(Type.class, dto.getStatutProjetCode()) : null;

        // Use the mapper to create the entity
        Project project = projectMapper.toEntity(dto, typeProjet, frequencePrelevement, statutProjet);

        projectRepo.persist(project);
        return project;
    }

    @Override
    @Transactional
    public Project updateProject(UpdateProjectDTO dto) {
        Project project = projectRepo.findByProjetId(dto.getProjectId());
        if (project == null) {
            throw new IllegalArgumentException("Projet non trouvé avec l'ID: " + dto.getProjectId());
        }

        // Get Type entities from database using the codes
        // The validation is already done by the validators
        Type typeProjet = em.find(Type.class, dto.getTypeProjetCode());
        Type frequencePrelevement = dto.getFrequencePrelevementCode() != null ? em.find(Type.class, dto.getFrequencePrelevementCode()) : null;
        Type statutProjet = dto.getStatutProjetCode() != null ? em.find(Type.class, dto.getStatutProjetCode()) : null;

        // Use the mapper to update the entity
        project = projectMapper.updateEntity(project, dto, typeProjet, frequencePrelevement, statutProjet);

        projectRepo.persist(project);
        return project;
    }

    @Override
    public Page<ReadProjectDTO> searchProjects(String key, PageRequest pageable) {
        Page<Project> projectsPage = projectRepo.searchProjects(key, pageable);

        List<ReadProjectDTO> dtos = new ArrayList<>();
        for (Project project : projectsPage.getContent()) {
            dtos.add(mapToReadDTO(project));
        }

        return new Page<>(dtos, projectsPage.getTotalElements(), pageable.getPage(), pageable.getSize());
    }

    @Override
    public ReadProjectDTO findById(Long projetId) {
        Project project = projectRepo.findByProjectId(projetId);
        if (project == null) {
            throw new IllegalArgumentException("Projet non trouvé avec l'ID: " + projetId);
        }
        return mapToReadDTO(project);
    }

    @Override
    public Page<ReadProjectDTO> searchLandSaleProjects(String key, PageRequest pageable) {
        return searchProjectsByType(key, pageable, TYPE_LAND_SALE);
    }

    @Override
    public Page<ReadProjectDTO> searchHousingAcquisitionProjects(String key, PageRequest pageable) {
        return searchProjectsByType(key, pageable, TYPE_HOUSING_ACQUISITION);
    }

    @Override
    public Page<ReadProjectDTO> searchRepayableLoanProjects(String key, PageRequest pageable) {
        return searchProjectsByType(key, pageable, TYPE_REPAYABLE_LOAN);
    }

    @Override
    public byte[] generateProjectReport(Long projetId) throws Exception {
        // This would typically generate a PDF or other report format
        // For now, we'll just return a placeholder
        return ("Rapport de projet pour l'ID: " + projetId).getBytes();
    }

    // Helper method to search projects by type
    private Page<ReadProjectDTO> searchProjectsByType(String key, PageRequest pageable, String typeCode) {
        Page<Project> projectsPage = projectRepo.searchProjectsByType(key, pageable, typeCode);

        List<ReadProjectDTO> dtos = new ArrayList<>();
        for (Project project : projectsPage.getContent()) {
            dtos.add(mapToReadDTO(project));
        }

        return new Page<>(dtos, projectsPage.getTotalElements(), pageable.getPage(), pageable.getSize());
    }

    // Helper method to map Project entity to ReadProjectDTO
    private ReadProjectDTO mapToReadDTO(Project project) {
        return projectMapper.toReadDTO(project);
    }
}
