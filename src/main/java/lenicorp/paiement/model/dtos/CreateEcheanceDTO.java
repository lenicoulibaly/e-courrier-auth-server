package lenicorp.paiement.model.dtos;

import lombok.*;
import lenicorp.paiement.model.validators.CoherentEcheanceDates;
import lenicorp.paiement.model.validators.ExistingEcheancierId;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@CoherentEcheanceDates
public class CreateEcheanceDTO
{
    private LocalDate dateEcheance;
    private LocalDate dateButtoire;
    private BigDecimal montantEcheance;
    private BigDecimal tauxEcheance;
    private String nomEcheance;
    private String frequenceUniqueCode;
    @ExistingEcheancierId
    private Long echeancierId;

    public CreateEcheanceDTO(LocalDate dateEcheance, Long echeancierId, String frequenceUniqueCode) {
        this.dateEcheance = dateEcheance;
        this.echeancierId = echeancierId;
        this.frequenceUniqueCode = frequenceUniqueCode;
    }
}
