package lenicorp.project.model.mappers;

import lenicorp.project.model.dtos.CreateProjectDTO;
import lenicorp.project.model.dtos.ReadProjectDTO;
import lenicorp.project.model.dtos.UpdateProjectDTO;
import lenicorp.project.model.entities.Project;
import lenicorp.types.model.entities.Type;

/**
 * Mapper interface for Project entity
 */
public interface ProjectMapper {
    
    /**
     * Maps a Project entity to a ReadProjectDTO
     * @param project the Project entity to map
     * @return the mapped ReadProjectDTO
     */
    ReadProjectDTO toReadDTO(Project project);
    
    /**
     * Maps a CreateProjectDTO to a Project entity
     * @param dto the CreateProjectDTO to map
     * @param typeProjet the Type entity for the project type
     * @param frequencePrelevement the Type entity for the frequency of payments
     * @param statutProjet the Type entity for the project status
     * @return the mapped Project entity
     */
    Project toEntity(CreateProjectDTO dto, Type typeProjet, Type frequencePrelevement, Type statutProjet);
    
    /**
     * Updates a Project entity from an UpdateProjectDTO
     * @param project the Project entity to update
     * @param dto the UpdateProjectDTO with the new values
     * @param typeProjet the Type entity for the project type
     * @param frequencePrelevement the Type entity for the frequency of payments
     * @param statutProjet the Type entity for the project status
     * @return the updated Project entity
     */
    Project updateEntity(Project project, UpdateProjectDTO dto, Type typeProjet, Type frequencePrelevement, Type statutProjet);
}