package lenicorp.paiement.model.mappers;

import lenicorp.paiement.model.dtos.PaiementCotisationDTO;
import lenicorp.paiement.model.dtos.VersementDTO;
import lenicorp.paiement.model.entities.Paiement;
import lenicorp.paiement.model.entities.Versement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "cdi")
public interface PaiementMapper
{
    Paiement mapToPaiementCotisation(PaiementCotisationDTO dto);

    @Mapping(target = "datePaiement", source = "versement.dateVersement")
    @Mapping(target = "modePaiementCode", source = "versement.modePaiement.code")
    @Mapping(target = "modePaiement", source = "versement.modePaiement.name")
    @Mapping(target = "typePaiementCode", source = "versement.typePaiement.code")
    @Mapping(target = "typePaiement", source = "versement.typePaiement.name")
    PaiementCotisationDTO mapToPaiementDTO(Paiement paiement);

    @Mapping(target = "dateVersement", source = "datePaiement")
    @Mapping(target = "modePaiement", expression = "java(new lenicorp.types.model.entities.Type(dto.getModePaiementCode()))")
    @Mapping(target = "typePaiement", expression = "java(new lenicorp.types.model.entities.Type(dto.getTypePaiementCode()))")
    @Mapping(target = "souscription", expression = "java(null)")
    Versement mapToVersement(PaiementCotisationDTO dto);

    @Mapping(target = "modePaiement", source = "modePaiement.name")
    @Mapping(target = "modePaiementCode", source = "modePaiement.code")
    @Mapping(target = "typePaiement", source = "typePaiement.name")
    @Mapping(target = "typePaiementCode", source = "typePaiement.code")
    VersementDTO mapToVersementDto(Versement versement);
}
