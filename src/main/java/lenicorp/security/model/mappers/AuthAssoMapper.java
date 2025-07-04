package lenicorp.security.model.mappers;

import lenicorp.security.model.dtos.UserProfileAssoDTO;
import lenicorp.security.model.entities.AuthAssociation;
import lenicorp.types.model.entities.Type;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.CDI)
public interface AuthAssoMapper
{
    @Mapping(source = "strName", target = "structure.strName")
    @Mapping(source = "strId", target = "structure.strId")
    @Mapping(source = "profileName", target = "profile.name")
    @Mapping(source = "profileCode", target = "profile.code")
    @Mapping(source = "email", target = "user.email")
    @Mapping(source = "userId", target = "user.userId")
    @Mapping(expression = "java(mapToType(\"USR_PRFL\"))", target = "type")
    AuthAssociation toEntity(UserProfileAssoDTO userProfileAssoDTO);

    @InheritInverseConfiguration(name = "toEntity")
    UserProfileAssoDTO toDto(AuthAssociation authAssociation);

    @InheritConfiguration(name = "toEntity")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    AuthAssociation partialUpdate(UserProfileAssoDTO userProfileAssoDTO, @MappingTarget AuthAssociation authAssociation);

    default Type mapToType(String typeCode)
    {
        if (typeCode == null) return null;
        return new Type(typeCode);
    }
}