package lenicorp.paiement.model.mappers;

import jakarta.inject.Inject;
import lenicorp.paiement.controller.repositories.spec.IEcheancierRepo;
import lenicorp.paiement.model.dtos.CreateEcheanceDTO;
import lenicorp.paiement.model.dtos.ReadEcheanceDTO;
import lenicorp.paiement.model.entities.Echeance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "cdi")
public abstract class EcheanceMapper
{
    @Inject protected IEcheancierRepo echeancierRepo;
    @Mapping(target = "echeancier", expression = "java(dto.getEcheancierId() == null ? null : new lenicorp.paiement.model.entities.Echeancier(dto.getEcheancierId()))")
    public abstract Echeance mapToEcheance(CreateEcheanceDTO dto);

    @Mapping(target = "echeancierId", source = "echeancier.echeancierId")
    @Mapping(target = "echeancierFrequenceCode", source = "echeancier.frequence.code")
    @Mapping(target = "echeancierFrequenceName", source = "echeancier.frequence.name")
    @Mapping(target = "echeancierName", source = "echeancier.name")
    @Mapping(target = "echeanceEchue", expression = "java(e.getDateEcheance() != null && e.getDateEcheance().isBefore(java.time.LocalDate.now()))")
    public abstract ReadEcheanceDTO mapToReadEcheanceDTO(Echeance e);
}
