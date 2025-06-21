package lenicorp.types.model.mappers;

import lenicorp.types.model.dtos.TypeGroupDTO;
import lenicorp.types.model.entities.TypeGroup;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "cdi")
public interface TypeGroupMapper {
    TypeGroupDTO mapToDto(TypeGroup entity);
    TypeGroup mapToEntity(TypeGroupDTO dto);

    TypeGroup updateTypeGroupFromDto(TypeGroupDTO dto, @MappingTarget TypeGroup t);
}
