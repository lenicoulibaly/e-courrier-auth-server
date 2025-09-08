package lenicorp.paiement.model.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lenicorp.archive.model.dtos.response.ReadDocDTO;
import lenicorp.association.model.validators.ExistingAdhesionId;
import lenicorp.paiement.model.validators.UniqueReference;
import lenicorp.paiement.model.validators.ValidModePaiement;
import lenicorp.paiement.model.validators.ValidTypePaiement;
import lenicorp.security.model.validators.ExistingUserId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@UniqueReference
public class PaiementCotisationDTO
{
    private Long PaiementId;
    private String reference;
    private LocalDate datePaiement;
    @NotNull(message = "Le montant du paiement ne peut être nul")
    @Positive(message = "Le montant du paiement doit être supérieur à 0")
    private BigDecimal montant;
    private String montantLettre;
    private boolean active;
    @ValidModePaiement
    @NotNull(message = "Le mode de paiement ne peut être nul")
    @NotBlank(message = "Le mode de paiement ne peut être nul")
    private String modePaiementCode;
    private String modePaiement;
    @ValidTypePaiement
    @NotNull(message = "Le type de paiement ne peut être nul")
    @NotBlank(message = "Le type de paiement ne peut être nul")
    private String typePaiementCode;
    private String typePaiement;
    @ExistingAdhesionId
    private Long adhesionId;
    @ExistingUserId
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String nomCotisation;
    private String motif;
    private Long versementId;
    private String codeVersement;

    private String echeanceCoursPaiement;
    private BigDecimal retardPaiement;
    private Long nbrEcheancesSoldeesParCeVersement;
    private String prochaineEcheance;
    private BigDecimal montantProchaineEcheance;
    private BigDecimal montantVersementSouhaite;
    private List<ReadDocDTO> documents;

    public PaiementCotisationDTO(Long paiementId, String reference, BigDecimal montant, String montantLettre, String echeanceCoursPaiement) {
        PaiementId = paiementId;
        this.reference = reference;
        this.montant = montant;
        //this.montantLettre = StringUtils.capitalize(montantLettre);
        //this.echeanceCoursPaiement = StringUtils.capitalize(echeanceCoursPaiement) ;
    }
}