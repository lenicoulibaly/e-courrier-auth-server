package lenicorp.types.model.mappers;

import lenicorp.types.model.dtos.TypeDTO;
import lenicorp.types.model.entities.Type;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "cdi")
public interface TypeMapper
{
    @Mapping(target = "typeGroup", expression = "java(new lenicorp.types.model.entities.TypeGroup(dto.getGroupCode()))")
    @Mapping(target = "code", expression = "java(dto.getCode().toUpperCase())")
    Type mapToType(TypeDTO dto);

    @Mapping(target = "groupCode", source = "typeGroup.groupCode")
    TypeDTO mapToDto(Type type);

    @Mapping(target = "typeGroup", expression = "java(new lenicorp.types.model.entities.TypeGroup(dto.getGroupCode()))")
    @Mapping(target = "code", ignore = true)
    Type mapToType(TypeDTO dto, @MappingTarget Type entity);
}