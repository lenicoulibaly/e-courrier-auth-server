package lenicorp.paiement.model.entities;

import jakarta.persistence.*;
import lenicorp.project.model.entities.Souscription;
import lenicorp.security.audit.AuditableEntity;
import lenicorp.types.model.entities.Type;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.math.BigDecimal;
import java.time.LocalDate;

import static lenicorp.utilities.PRECISION.QUARANTE_INT;
import static lenicorp.utilities.PRECISION.VINGT_INT;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter
@Entity @Audited
public class Versement extends AuditableEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "VERS_ID_GEN")
    @SequenceGenerator(name = "VERS_ID_GEN", sequenceName = "VERS_ID_GEN")
    private Long versementId;
    private String codeVersement;
    private LocalDate dateVersement;
    @Column(precision = QUARANTE_INT, scale = VINGT_INT)
    private BigDecimal montant;
    private String montantLettre;
    private boolean active;
    @ManyToOne
    @JoinColumn(name = "mode_paiement_unique_code")
    private Type modePaiement;
    @ManyToOne @JoinColumn(name = "type_paiement_unicode_code")
    private Type typePaiement;//Paiement cotisation, paiement acquisition terrain, paiement acquisition logement, paiement prêt scolaire
    @ManyToOne @JoinColumn(name = "souscription_id")
    private Souscription souscription;

    public Versement(Long versementId)
    {
        this.versementId = versementId;
    }
}
