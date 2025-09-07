package lenicorp.project.model.mappers;

import jakarta.enterprise.context.ApplicationScoped;
import lenicorp.project.model.dtos.CreateProjectDTO;
import lenicorp.project.model.dtos.ReadProjectDTO;
import lenicorp.project.model.dtos.UpdateProjectDTO;
import lenicorp.project.model.entities.Project;
import lenicorp.types.model.entities.Type;

/**
 * Implementation of the ProjectMapper interface
 */
@ApplicationScoped
public class ProjectMapperImpl implements ProjectMapper {

    // Constants for project types
    public static final String TYPE_LAND_SALE = "LAND_SALE";
    public static final String TYPE_HOUSING_ACQUISITION = "HOUSING_ACQUISITION";
    public static final String TYPE_REPAYABLE_LOAN = "REPAYABLE_LOAN";

    @Override
    public ReadProjectDTO toReadDTO(Project project) {
        if (project == null) {
            return null;
        }

        // Get type code and name
        String typeProjetCode = project.getTypeProjet() != null ? project.getTypeProjet().code : null;
        String typeProjetNom = project.getTypeProjet() != null ? project.getTypeProjet().name : null;

        // Get statut projet code and name
        String statutProjetCode = project.getStatutProjet() != null ? project.getStatutProjet().code : null;
        String statutProjetNom = project.getStatutProjet() != null ? project.getStatutProjet().name : null;

        // Get frequence prelevement code and name
        String frequencePrelevementCode = project.getFrequencePrelevement() != null ? project.getFrequencePrelevement().code : null;
        String frequencePrelevementNom = project.getFrequencePrelevement() != null ? project.getFrequencePrelevement().name : null;

        ReadProjectDTO dto = new ReadProjectDTO(
            project.getProjectId(),
            project.getNomProjet(),
            project.getDescription(),
            typeProjetCode,
            typeProjetNom,
            project.getDebutProjet(),
            project.getFinProjet(),
            project.getLocalisation(),
            statutProjetCode,
            statutProjetNom
        );

        // Set additional fields
        dto.setDebutPrelevement(project.getDebutPrelevement());
        dto.setDureePrelevementsMois(project.getDureePrelevementsMois());
        dto.setFinPrelevement(project.getFinPrelevement());
        dto.setFrequencePrelevementCode(frequencePrelevementCode);
        dto.setFrequencePrelevementNom(frequencePrelevementNom);
        dto.setSearchText(project.getSearchText());

        // Set audit fields
        dto.setCreatedBy(project.getCreatedBy());
        dto.setCreatedAt(project.getCreatedAt() != null ? project.getCreatedAt().toLocalDate() : null);

        // Set type-specific fields
        if (typeProjetCode != null) {
            if (TYPE_LAND_SALE.equals(typeProjetCode)) {
                dto.setSuperficieTotale(project.getSuperficieTotale());
            } else if (TYPE_REPAYABLE_LOAN.equals(typeProjetCode)) {
                dto.setMontantTotalPret(project.getMontantTotalPret());
                dto.setTauxInteret(project.getTauxInteret());
            }
        }

        return dto;
    }

    @Override
    public Project toEntity(CreateProjectDTO dto, Type typeProjet, Type frequencePrelevement, Type statutProjet) {
        if (dto == null) {
            return null;
        }

        Project project = new Project();
        project.setNomProjet(dto.getNomProjet());
        project.setDescription(dto.getDescription());
        project.setTypeProjet(typeProjet);
        project.setDebutProjet(dto.getDebutProjet());
        project.setFinProjet(dto.getFinProjet());
        project.setDebutPrelevement(dto.getDebutPrelevement());
        project.setDureePrelevementsMois(dto.getDureePrelevementsMois());
        project.setFrequencePrelevement(frequencePrelevement);
        project.setLocalisation(dto.getLocalisation());
        project.setStatutProjet(statutProjet);

        // Set fields based on project type
        String typeCode = dto.getTypeProjetCode();
        if (TYPE_LAND_SALE.equals(typeCode)) {
            project.setSuperficieTotale(dto.getSuperficieTotale());
        } else if (TYPE_REPAYABLE_LOAN.equals(typeCode)) {
            project.setMontantTotalPret(dto.getMontantTotalPret());
            project.setTauxInteret(dto.getTauxInteret());
        }

        return project;
    }

    @Override
    public Project updateEntity(Project project, UpdateProjectDTO dto, Type typeProjet, Type frequencePrelevement, Type statutProjet) {
        if (project == null || dto == null) {
            return project;
        }

        project.setNomProjet(dto.getNomProjet());
        project.setDescription(dto.getDescription());
        project.setTypeProjet(typeProjet);
        project.setDebutProjet(dto.getDebutProjet());
        project.setFinProjet(dto.getFinProjet());
        project.setDebutPrelevement(dto.getDebutPrelevement());
        project.setDureePrelevementsMois(dto.getDureePrelevementsMois());
        project.setFrequencePrelevement(frequencePrelevement);
        project.setLocalisation(dto.getLocalisation());
        project.setStatutProjet(statutProjet);

        // Update fields based on project type
        String typeCode = dto.getTypeProjetCode();
        if (TYPE_LAND_SALE.equals(typeCode)) {
            project.setSuperficieTotale(dto.getSuperficieTotale());
            // Clear other type-specific fields
            project.setMontantTotalPret(null);
            project.setTauxInteret(null);
        } else if (TYPE_REPAYABLE_LOAN.equals(typeCode)) {
            project.setMontantTotalPret(dto.getMontantTotalPret());
            project.setTauxInteret(dto.getTauxInteret());
            // Clear other type-specific fields
            project.setSuperficieTotale(null);
        }

        return project;
    }
}
