package lenicorp.association.model.mappers;

import lenicorp.association.model.dtos.CreateSectionDTO;
import lenicorp.association.model.dtos.ReadSectionDTO;
import lenicorp.association.model.entities.Association;
import lenicorp.association.model.entities.Section;
import lenicorp.structures.model.entities.Structure;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.CDI)
public interface SectionMapper
{
    @Mapping(target = "association", expression = "java(dto.getAssoId() == null ? null : new lenicorp.association.model.entities.Association(dto.getAssoId()))")
    @Mapping(target = "strTutelle", expression = "java(dto.getStrId() == null ? null : new lenicorp.structures.model.entities.Structure(dto.getStrId()))")
    Section mapToSection(CreateSectionDTO dto);

    @Mapping(target = "assoId", source = "association.assoId")
    @Mapping(target = "assoName", source = "association.assoName")
    @Mapping(target = "strName", source = "strTutelle.strName")
    @Mapping(target = "strSigle", source = "strTutelle.strSigle")
    ReadSectionDTO mapToReadSectionDTO(Section section);
}
