package lenicorp.association.controller.services;

import lenicorp.association.model.dtos.CreateSectionDTO;
import lenicorp.association.model.dtos.ReadSectionDTO;
import lenicorp.association.model.dtos.UpdateSectionDTO;
import lenicorp.association.model.entities.Association;
import lenicorp.association.model.entities.Section;
import lenicorp.utilities.Page;
import lenicorp.utilities.PageRequest;

import java.util.List;

public interface ISectionService {
    ReadSectionDTO createSection(CreateSectionDTO dto);

    ReadSectionDTO updateSection(UpdateSectionDTO dto);

    Page<ReadSectionDTO> searchSections(String key, Long assoId, Long strId, PageRequest pageable);

    Section createSectionDeBase(Association association);

    List<ReadSectionDTO> getAssociationSections(Long assoId);
}
