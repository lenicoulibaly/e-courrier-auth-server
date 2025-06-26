package lenicorp.structures.controller.service;

import lenicorp.structures.model.dtos.ChangeAnchorDTO;
import lenicorp.structures.model.dtos.CreateOrUpdateStrDTO;
import lenicorp.structures.model.dtos.ReadStrDTO;
import lenicorp.utilities.Page;
import lenicorp.utilities.PageRequest;

import java.util.List;

public interface IStrService
{
    ReadStrDTO createStr(CreateOrUpdateStrDTO dto);

    ReadStrDTO updateStr(CreateOrUpdateStrDTO dto);

    ReadStrDTO changeAnchor(ChangeAnchorDTO dto);

    Page<ReadStrDTO> searchStrs(String key, Long parentId, String typeCode, PageRequest pageRequest);

    List<ReadStrDTO> getPossibleParentStructures(String childTypeCode);
    List<ReadStrDTO> getRootStructures();
    CreateOrUpdateStrDTO getUpdateDto(Long strId);

    ChangeAnchorDTO getChangeAnchorDto(Long strId);

}
