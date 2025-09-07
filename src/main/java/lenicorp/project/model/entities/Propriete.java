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
import java.util.ArrayList;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Audited @Entity
@Table(
    name = "propriete",
    indexes = {
        @Index(name = "idx_propriete_project", columnList = "project_id"),
        @Index(name = "idx_propriete_type", columnList = "type_code")
    }
)
public class Propriete extends AuditableEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROPRIETE_ID_GEN")
    @SequenceGenerator(name = "PROPRIETE_ID_GEN", sequenceName = "PROPRIETE_ID_GEN", allocationSize = 10)
    private Long proprieteId;

    @ManyToOne(optional = false) @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(nullable = false)
    private String reference; // Ex: Lot A12, Maison B5...

    private String description;
    private Long qteInitiale;

    private BigDecimal superficie; // en m2 (utile pour lots/terrains)
    private Integer nombrePieces; // utile pour maison/appartements

    private BigDecimal prixUnitaire;

    @ManyToOne @JoinColumn(name = "type_code", nullable = false)
    private Type typePropriete;

    @OneToMany(mappedBy = "propriete", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Caracteristique> caracteristiques = new ArrayList<>();

    public Propriete(Long proprieteId) {
        this.proprieteId = proprieteId;
        this.caracteristiques = new ArrayList<>();
    }

    // Helper methods to manage bidirectional relationship
    public void addCaracteristique(Caracteristique caracteristique) {
        caracteristiques.add(caracteristique);
        caracteristique.setPropriete(this);
    }

    public void removeCaracteristique(Caracteristique caracteristique) {
        caracteristiques.remove(caracteristique);
        caracteristique.setPropriete(null);
    }

    @Override
    public String toString() {
        return proprieteId + "_" + reference + "_" + (project != null ? project.getNomProjet() : "");
    }
}
