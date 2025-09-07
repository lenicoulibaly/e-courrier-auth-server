package lenicorp.project.model.entities;

import jakarta.persistence.*;
import lenicorp.security.audit.AuditableEntity;
import lenicorp.types.model.entities.Type;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Audited
@Entity
@Table(
        name = "project",
        indexes = {
                @Index(name = "idx_project_type", columnList = "project_type_code"),
                @Index(name = "idx_project_statut", columnList = "statut_code"),
                @Index(name = "idx_project_dates", columnList = "debutProjet, finProjet")
        }
)
public class Project extends AuditableEntity
{
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROJECT_ID_GEN")
    @SequenceGenerator(name = "PROJECT_ID_GEN", sequenceName = "PROJECT_ID_GEN", allocationSize = 10)
    private Long projectId;

    @Column(nullable = false)
    private String nomProjet;

    @Column(nullable = false)
    private String description;

    @ManyToOne @JoinColumn(name = "project_type_code", nullable = false)
    private Type typeProjet;

    private LocalDate debutProjet;
    private LocalDate finProjet;

    private LocalDate debutPrelevement;
    private Integer dureePrelevementsMois;
    @Transient
    private LocalDate finPrelevement;
    @ManyToOne @JoinColumn(name = "frequence_prelevement_code", nullable = false)
    private Type frequencePrelevement;

    private String localisation;

    // Fields specific to land sales projects (Vente de terrain)
    private BigDecimal superficieTotale; // in square meters

    // Fields specific to repayable loan projects (Prêt remboursable)
    private BigDecimal montantTotalPret;
    private BigDecimal tauxInteret; // in percentage
    private String searchText;
    private BigDecimal fraisDossiers;
    private BigDecimal tauxApportInitiale;


    @ManyToOne @JoinColumn(name = "statut_code", nullable = false)
    private Type statutProjet;

    public Project(Long projectId) {
        this.projectId = projectId;
    }

    @Override
    public String toString() {
        return projectId + "_" + nomProjet + "_" + (typeProjet != null ? typeProjet.code : "");
    }
}
