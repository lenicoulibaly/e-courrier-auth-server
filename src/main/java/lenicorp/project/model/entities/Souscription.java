package lenicorp.project.model.entities;

import jakarta.persistence.*;
import lenicorp.security.audit.AuditableEntity;
import lenicorp.security.model.entities.AppUser;
import lenicorp.types.model.entities.Type;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Audited @Entity
@Table(
    name = "souscription",
    indexes = {
        @Index(name = "idx_souscription_propriete", columnList = "propriete_id"),
        @Index(name = "idx_souscription_statut", columnList = "statut_code"),
        @Index(name = "idx_souscription_dates", columnList = "date_souscription, date_fin")
    }
)
public class Souscription extends AuditableEntity 
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SOUSCRIPTION_ID_GEN")
    @SequenceGenerator(name = "SOUSCRIPTION_ID_GEN", sequenceName = "SOUSCRIPTION_ID_GEN", allocationSize = 10)
    private Long souscriptionId;

    @ManyToOne @JoinColumn(name = "propriete_id")
    private Propriete propriete;
    
    @Column(name = "date_souscription", nullable = false)
    private LocalDate dateSouscription;
    
    @ManyToOne @JoinColumn(name = "souscripteur_id", nullable = false)
    private AppUser souscripteur;
    
    @ManyToOne @JoinColumn(name = "statut_code", nullable = false)
    private Type statut;
    
    public Souscription(Long souscriptionId) {
        this.souscriptionId = souscriptionId;
    }

}