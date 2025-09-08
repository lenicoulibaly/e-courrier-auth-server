package lenicorp.paiement.model.entities;

import jakarta.persistence.*;
import lenicorp.security.audit.AuditableEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.math.BigDecimal;

import static lenicorp.utilities.PRECISION.QUARANTE_INT;
import static lenicorp.utilities.PRECISION.VINGT_INT;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter
@Entity
@Audited
public class Paiement extends AuditableEntity
{
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PAIE_ID_GEN")
  @SequenceGenerator(name = "PAIE_ID_GEN", sequenceName = "PAIE_ID_GEN")
  private Long paiementId;
  private String reference;
  @Column(precision = QUARANTE_INT, scale = VINGT_INT)
  private BigDecimal montant;
  private String montantLettre;
  private boolean active;

  @ManyToOne @JoinColumn(name = "echeance_id")
  private Echeance echeance;
  @ManyToOne @JoinColumn(name = "versement_id")
  private Versement versement;
}