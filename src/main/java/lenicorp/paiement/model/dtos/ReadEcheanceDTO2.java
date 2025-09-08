package lenicorp.paiement.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Simplified version of ReadEcheanceDTO for use in ReadEcheancierDTO
 */
@AllArgsConstructor @NoArgsConstructor @Getter @Setter
public class ReadEcheanceDTO2
{
    private Long echeanceId;
    private LocalDate dateEcheance;
    private String nomEcheance;
    private LocalDate dateButtoire;
    private BigDecimal montantEcheance;
    private BigDecimal tauxEcheance;
    private Long echeancierId;
    private boolean echeanceEchue;
}