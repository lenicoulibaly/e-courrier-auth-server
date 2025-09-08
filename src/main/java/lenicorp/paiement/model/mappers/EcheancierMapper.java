package lenicorp.paiement.model.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import lenicorp.paiement.model.dtos.CreateEcheancierDTO;
import lenicorp.paiement.model.dtos.ReadEcheancierDTO;
import lenicorp.paiement.model.entities.Echeancier;

@Mapper(componentModel = "cdi")
public interface EcheancierMapper
{
    @Mapping(target = "frequence", expression = "java(dto.getFrequenceTypeCode() == null ? null : new lenicorp.types.model.entities.Type(dto.getFrequenceTypeCode()))")
    Echeancier mapToEcheancier(CreateEcheancierDTO dto);

    @Mapping(target = "typeEcheancierCode", expression = "java(null)")
    @Mapping(target = "typeEcheancierName", expression = "java(null)")
    @Mapping(target = "frequenceTypeCode", source = "frequence.code")
    @Mapping(target = "frequenceTypeName", source = "frequence.name")
    @Mapping(target = "exeCode", expression = "java(null)")
    ReadEcheancierDTO mapToReadEcheancierDTO(Echeancier echeancier);
}
