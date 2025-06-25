package lenicorp.structures.controller.service;

import lenicorp.structures.model.dtos.ChangeAnchorDTO;
import lenicorp.structures.model.dtos.CreateOrUpdateStrDTO;
import lenicorp.structures.model.dtos.ReadStrDTO;
import lenicorp.utilities.Page;
import lenicorp.utilities.PageRequest;

public interface IStrService
{
    ReadStrDTO createStr(CreateOrUpdateStrDTO dto);
    ReadStrDTO updateStr(CreateOrUpdateStrDTO dto);
    ReadStrDTO changeAncrage(ChangeAnchorDTO dto);
    Page<ReadStrDTO> searchStrs(String key, Long parentId, String typeCode, PageRequest pageRequest);
}
