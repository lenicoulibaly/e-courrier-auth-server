package lenicorp.association.controller.repositories.spec;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import lenicorp.association.model.dtos.ReadAssociationDTO;
import lenicorp.association.model.entities.Association;
import lenicorp.utilities.Page;
import lenicorp.utilities.PageRequest;

public interface IAssoRepo extends PanacheRepository<Association>
{
    Page<ReadAssociationDTO> searchAssociations(String key, PageRequest pageRequest);
    String getStrCode(Long strId);
    boolean existsByName(String assoName);
    boolean existsByName(String assoName, Long assoId);
    ReadAssociationDTO findReadAssoDtoById(Long assoId);
    String getSigleByAssoId(Long assoId);
    boolean existsById(Long id);
}
