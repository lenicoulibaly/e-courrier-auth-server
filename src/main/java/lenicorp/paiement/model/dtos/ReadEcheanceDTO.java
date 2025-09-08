package lenicorp.paiement.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter
public class ReadEcheanceDTO
{
    private Long echeanceId;
    private LocalDate dateEcheance;
    private String nomEcheance;
    private LocalDate dateButtoire;
    private BigDecimal montantEcheance;
    private BigDecimal tauxEcheance;
    private Long echeancierId;
    private String echeancierFrequenceCode;
    private String echeancierFrequenceName;
    private String echeancierName;
    private Long exeCode;
    private boolean echeanceEchue;

    public ReadEcheanceDTO(Long echeanceId, LocalDate dateEcheance, Long echeancierId, boolean echeanceEchue)
    {
        this.echeanceId = echeanceId;
        this.dateEcheance = dateEcheance;
        this.echeancierId = echeancierId;
        this.echeanceEchue =echeanceEchue;
    }
    public ReadEcheanceDTO(Long echeanceId, LocalDate dateEcheance, String nomEcheance, Long echeancierId)
    {
        this.echeanceId = echeanceId;
        this.dateEcheance = dateEcheance;
        this.nomEcheance = nomEcheance;
        this.echeancierId = echeancierId;
    }

    public ReadEcheanceDTO(Long echeanceId, LocalDate dateEcheance, String nomEcheance, Long echeancierId, boolean echeanceEchue)
    {
        this.echeanceId = echeanceId;
        this.dateEcheance = dateEcheance;
        this.nomEcheance = nomEcheance;
        this.echeancierId = echeancierId;
        this.echeanceEchue =echeanceEchue;
    }
}
