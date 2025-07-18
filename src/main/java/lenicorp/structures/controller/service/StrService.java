package lenicorp.structures.controller.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lenicorp.structures.controller.repositories.IStrRepo;
import lenicorp.structures.controller.repositories.StrRepo;
import lenicorp.structures.controller.repositories.VStrRepo;
import lenicorp.structures.model.dtos.ChangeAnchorDTO;
import lenicorp.structures.model.dtos.CreateOrUpdateStrDTO;
import lenicorp.structures.model.dtos.ReadStrDTO;
import lenicorp.structures.model.dtos.StrMapper;
import lenicorp.structures.model.entities.Structure;
import lenicorp.utilities.Page;
import lenicorp.utilities.PageRequest;

import java.util.List;

@ApplicationScoped
public class StrService implements IStrService
{
    @Inject private VStrRepo vsRepo;
    @Inject private IStrRepo strRepo;
    @Inject private StrMapper strMapper;

    @Override @Transactional
    public ReadStrDTO createStr(CreateOrUpdateStrDTO dto)
    {
        Structure str = strMapper.mapToStructureForCreate(dto);
        strRepo.persist(str);
        return strMapper.mapToReadStrDTO(str);
    }

    @Override
    @Transactional
    public ReadStrDTO updateStr(CreateOrUpdateStrDTO dto)
    {
        Structure str = strRepo.findById(dto.getStrId());
        str = strMapper.updateStructureFromDTO(dto, str);
        strRepo.persist(str);
        return strMapper.mapToReadStrDTO(str);
    }

    @Override @Transactional //TODO à implémenter et à valider
    public ReadStrDTO changeAnchor(ChangeAnchorDTO dto)
    {
        Structure str = strRepo.findById(dto.getStrId());
        str = strMapper.updateParentFromChangeAnchorDto(dto, str);
        strRepo.persist(str);
        return strMapper.mapToReadStrDTO(str);
    }

    @Override
    public Page<ReadStrDTO> searchStrs(String key, Long parentId, String typeCode, PageRequest pageRequest)
    {
        return vsRepo.search(key, parentId, typeCode, pageRequest);
    }

    @Override
    public List<ReadStrDTO> getPossibleParentStructures(String childTypeCode)
    {
        return strRepo.getPossibleParentStructures(childTypeCode);
    }

    @Override
    public List<ReadStrDTO> getRootStructures()
    {
        return strRepo.getRootStructures();
    }

    @Override
    public CreateOrUpdateStrDTO getUpdateDto(Long strId)
    {
        return strRepo.getUpdateDto(strId);
    }

    @Override
    public ChangeAnchorDTO getChangeAnchorDto(Long strId)
    {
        return strRepo.getChangeAnchorDto(strId);
    }

}