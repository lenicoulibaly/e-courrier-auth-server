package lenicorp.paiement.model.entities;

import jakarta.persistence.*;
import lenicorp.security.audit.AuditableEntity;
import lenicorp.types.model.entities.Type;
import lombok.*;
import org.hibernate.envers.Audited;

import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Audited
@Entity
public class Echeancier extends AuditableEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ECHEANCIER_ID_GEN")
    @SequenceGenerator(name = "ECHEANCIER_ID_GEN", sequenceName = "ECHEANCIER_ID_GEN", allocationSize = 10)
    Long echeancierId;
    @ManyToOne @JoinColumn(name = "FREQ_TYPE_CODE")
    private Type frequence;
    private String name;
    @OneToMany(mappedBy = "echeancier" , cascade = CascadeType.ALL, orphanRemoval = true)
    List<Echeance> echeances;

    public Echeancier(Long echeancierId) {
        this.echeancierId = echeancierId;
    }
}
