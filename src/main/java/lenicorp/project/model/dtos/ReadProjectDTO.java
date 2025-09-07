package lenicorp.project.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ReadProjectDTO
{
    private Long projectId;
    private String nomProjet;
    private String description;
    private String typeProjetCode;
    private String typeProjetNom;
    private LocalDate debutProjet;
    private LocalDate finProjet;
    private LocalDate debutPrelevement;
    private Integer dureePrelevementsMois;
    private LocalDate finPrelevement;
    private String frequencePrelevementCode;
    private String frequencePrelevementNom;
    private String localisation;

    // Fields specific to land sales projects (Vente de terrain)
    private BigDecimal superficieTotale;

    // Fields specific to repayable loan projects (Prêt remboursable)
    private BigDecimal montantTotalPret;
    private BigDecimal tauxInteret;

    private String statutProjetCode;
    private String statutProjetNom;
    private String searchText;

    private String createdBy;
    private LocalDate createdAt;
    private String lastModifiedBy;
    private LocalDate lastModifiedAt;

    // Constructor with essential fields
    public ReadProjectDTO(Long projectId, String nomProjet, String description, 
                          String typeProjetCode, String typeProjetNom, LocalDate debutProjet, LocalDate finProjet, 
                          String localisation, String statutProjetCode, String statutProjetNom) {
        this.projectId = projectId;
        this.nomProjet = nomProjet;
        this.description = description;
        this.typeProjetCode = typeProjetCode;
        this.typeProjetNom = typeProjetNom;
        this.debutProjet = debutProjet;
        this.finProjet = finProjet;
        this.localisation = localisation;
        this.statutProjetCode = statutProjetCode;
        this.statutProjetNom = statutProjetNom;
    }
}
