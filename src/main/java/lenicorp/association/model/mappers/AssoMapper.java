package lenicorp.association.model.mappers;

import lenicorp.association.model.dtos.CreateAssociationDTO;
import lenicorp.association.model.entities.Association;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.CDI)
public abstract class AssoMapper
{
    public abstract Association mapToAssociation(CreateAssociationDTO dto);
}