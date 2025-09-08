package lenicorp.paiement.model.dtos;

import lombok.*;
import lenicorp.paiement.model.validators.CoherentEcheanceDates;
import lenicorp.paiement.model.validators.ExistingEcheanceId;
import lenicorp.paiement.model.validators.ExistingEcheancierId;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@CoherentEcheanceDates
public class UpdateEcheanceDTO
{
    @ExistingEcheanceId
    private Long echeanceId;
    private LocalDate dateEcheance;
    private LocalDate dateButtoire;
    private BigDecimal montantEcheance;
    private BigDecimal tauxEcheance;
    private String nomEcheance;
    @ExistingEcheancierId
    private Long echeancierId;
}
