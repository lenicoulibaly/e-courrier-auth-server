package lenicorp.types.model.mappers;

import lenicorp.types.model.dtos.TypeDTO;
import lenicorp.types.model.entities.Type;
import lenicorp.types.model.entities.TypeGroup;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "cdi")
public interface TypeMapper
{
    @Mapping(target = "typeGroup", expression = "java(new lenicorp.types.model.entities.TypeGroup(dto.getGroupCode()))")
    @Mapping(target = "code", expression = "java(dto.getCode().toUpperCase())")
    Type mapToType(TypeDTO dto);

    @Mapping(target = "groupCode", source = "typeGroup.groupCode")
    TypeDTO mapToDto(Type type);

    @Mapping(target = "typeGroup", source="groupCode", qualifiedByName = "mapToTypeGroup")
    @Mapping(target = "code", ignore = true)
    Type mapToType(TypeDTO dto, @MappingTarget Type entity);

    @Named("normalizeCode")
    default String normalizeCode(String code) {
        return code != null ? code.toUpperCase().trim() : null;
    }

    @Named("mapToTypeGroup")
    default TypeGroup mapToTypeGroup(String groupCode)
    {
        if (groupCode == null) return null;
        return new TypeGroup(normalizeCode(groupCode));
    }

}