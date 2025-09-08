package lenicorp.paiement.model.entities;

import jakarta.persistence.*;
import lenicorp.security.audit.AuditableEntity;
import lombok.*;
import org.hibernate.envers.Audited;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Audited
@Entity
public class Echeance extends AuditableEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ECHEANCE_ID_GEN")
    @SequenceGenerator(name = "ECHEANCE_ID_GEN", sequenceName = "ECHEANCE_ID_GEN", allocationSize = 10)
    private Long echeanceId;
    private LocalDate dateEcheance;
    private LocalDate dateButtoire;
    private BigDecimal montantEcheance;
    private BigDecimal tauxEcheance;
    private String nomEcheance;
    @ManyToOne @JoinColumn(name = "ECHEANCIER_ID")
    private Echeancier echeancier;

    public Echeance(Long echeanceId) {
        this.echeanceId = echeanceId;
    }
}
