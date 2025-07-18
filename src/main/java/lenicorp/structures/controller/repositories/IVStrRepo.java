package lenicorp.structures.controller.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import lenicorp.structures.model.dtos.ReadStrDTO;
import lenicorp.structures.model.entities.VStructure;

import java.util.List;

/**
 * Interface for VStrRepo that defines methods for querying VStructure entities
 */
public interface IVStrRepo extends PanacheRepository<VStructure>
{

    /**
     * Find all descendants of a structure
     * @param strId The ID of the parent structure
     * @return List of descendant structures
     */
    List<ReadStrDTO> findAllDescendants(Long strId);

    /**
     * Get the chaineSigles for a structure
     * @param strId The ID of the structure
     * @return The chaineSigles
     */
    String getChaineSigles(Long strId);

    /**
     * Search for structures
     * @param key Search key
     * @param parentId Parent structure ID
     * @param typeCode Structure type code
     * @param pageRequest Pagination information
     * @return Page of structures
     */
    lenicorp.utilities.Page<ReadStrDTO> search(String key, Long parentId, String typeCode, lenicorp.utilities.PageRequest pageRequest);
}
