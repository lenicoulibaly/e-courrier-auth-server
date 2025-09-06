package lenicorp.association.controller.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lenicorp.association.controller.repositories.spec.ISectionRepo;
import lenicorp.association.model.dtos.CreateSectionDTO;
import lenicorp.association.model.dtos.ReadSectionDTO;
import lenicorp.association.model.dtos.UpdateSectionDTO;
import lenicorp.association.model.entities.Association;
import lenicorp.association.model.entities.Section;
import lenicorp.association.model.mappers.SectionMapper;
import lenicorp.exceptions.AppException;
import lenicorp.structures.model.entities.Structure;
import lenicorp.utilities.Page;
import lenicorp.utilities.PageRequest;

import java.util.List;
import java.util.Objects;

@ApplicationScoped
public class SectionService implements ISectionService
{
    @Inject
    ISectionRepo sectionRepo;

    @Inject
    SectionMapper sectionMapper;

    @Override 
    @Transactional
    public ReadSectionDTO createSection(CreateSectionDTO dto)
    {
        Section section = sectionMapper.mapToSection(dto);
        sectionRepo.persist(section);
        return sectionMapper.mapToReadSectionDTO(section);
    }

    @Override 
    @Transactional
    public ReadSectionDTO updateSection(UpdateSectionDTO dto)
    {
        if(dto == null) throw new AppException("Le corps de la requête est null");
        Section section = sectionRepo.findByIdOptional(dto.getSectionId()).orElseThrow(()->new AppException("Section introuvable"));
        Long dtoStrId = dto.getStrId();
        Long sectionStrId = section.getStrTutelle() == null ? null : section.getStrTutelle().getStrId();

        if(!Objects.equals(dtoStrId, sectionStrId))
            section.setStrTutelle(dtoStrId == null ? null : new Structure(dtoStrId));

        sectionRepo.persist(section);
        return sectionMapper.mapToReadSectionDTO(section);
    }

    @Override
    public Page<ReadSectionDTO> searchSections(String key, Long assoId, Long strId, PageRequest pageRequest) {
        return sectionRepo.searchSections(key, assoId, strId, pageRequest);
    }

    @Override
    @Transactional
    public Section createSectionDeBase(Association association) {
        Section sectionMere = new Section();
        sectionMere.setAssociation(association);
        sectionMere.setSectionName("Section de base");
        sectionMere.setSituationGeo(association.getSituationGeo());
        sectionRepo.persist(sectionMere);
        return sectionMere;
    }

    @Override
    public List<ReadSectionDTO> getAssociationSections(Long assoId)
    {
        return sectionRepo.findbyAssoId(assoId);
    }
}
