package lenicorp.structures.controller.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import lenicorp.structures.model.dtos.ChangeAnchorDTO;
import lenicorp.structures.model.dtos.CreateOrUpdateStrDTO;
import lenicorp.structures.model.dtos.ReadStrDTO;
import lenicorp.structures.model.entities.Structure;

import java.util.List;

public interface IStrRepo extends PanacheRepository<Structure>
{
    boolean parentHasCompatibleSousType(Long strParentId, String childTypeCode);

    boolean sigleExistsUnderSameParent(String sigle, Long parentId, Long excludeStrId);

    boolean existsById(Long strId);

    boolean strNameExistsUnderSameParent(String strName, Long parentId, Long excludeStrId);

    List<ReadStrDTO> getPossibleParentStructures(String childTypeCode);

    List<ReadStrDTO> getRootStructures();

    CreateOrUpdateStrDTO getUpdateDto(Long strId);

    ChangeAnchorDTO getChangeAnchorDto(Long strId);
}