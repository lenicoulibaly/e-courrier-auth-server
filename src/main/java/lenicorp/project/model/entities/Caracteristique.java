package lenicorp.project.model.entities;

import jakarta.persistence.*;
import lenicorp.security.audit.AuditableEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Audited @Entity
@Table(
    name = "caracteristique",
    indexes = {
        @Index(name = "idx_caracteristique_libelle", columnList = "libelle")
    }
)
public class Caracteristique extends AuditableEntity 
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CARACTERISTIQUE_ID_GEN")
    @SequenceGenerator(name = "CARACTERISTIQUE_ID_GEN", sequenceName = "CARACTERISTIQUE_ID_GEN", allocationSize = 10)
    private Long id;

    @Column(nullable = false)
    private String libelle;

    @Column(nullable = false)
    private String valeur;

    @ManyToOne @JoinColumn(name = "propriete_id") @JsonIgnore
    private Propriete propriete;

    public Caracteristique(Long id) {
        this.id = id;
    }

    public Caracteristique(String libelle, String valeur) {
        this.libelle = libelle;
        this.valeur = valeur;
    }

    @Override
    public String toString() {
        return id + "_" + libelle + "_" + valeur;
    }
}
