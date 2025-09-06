package lenicorp.association.model.mappers;

import lenicorp.association.model.dtos.AdhesionDTO;
import lenicorp.association.model.entities.Adhesion;
import lenicorp.security.model.entities.AppUser;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.CDI)
public abstract class AdhesionMapper
{
    public abstract Adhesion mapToAdhesion(AdhesionDTO dto);

    public abstract AdhesionDTO mapToAdhesionDto(AppUser user);
}
