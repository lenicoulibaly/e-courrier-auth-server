package lenicorp.project.controller.repositories.spec;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import lenicorp.project.model.entities.Project;
import lenicorp.utilities.Page;
import lenicorp.utilities.PageRequest;

/**
 * Repository interface for Project entities
 */
public interface IProjectRepo extends PanacheRepository<Project>
{
    /**
     * Find a project by ID
     * @param projetId the project ID
     * @return the project or null if not found
     */
    Project findByProjetId(Long projetId);

    /**
     * Find a project by ID (alias for findByProjetId)
     * @param projectId the project ID
     * @return the project or null if not found
     */
    default Project findByProjectId(Long projectId) {
        return findByProjetId(projectId);
    }

    /**
     * Search projects with pagination
     * @param key the search key
     * @param pageable the page request
     * @return a page of projects
     */
    Page<Project> searchProjects(String key, PageRequest pageable);

    /**
     * Search projects by type with pagination
     * @param key the search key
     * @param pageable the page request
     * @param typeCode the type code
     * @return a page of projects
     */
    Page<Project> searchProjectsByType(String key, PageRequest pageable, String typeCode);
}
