package lenicorp.project.model.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lenicorp.project.model.dtos.validator.ValidFrequencyType;
import lenicorp.project.model.dtos.validator.ValidProjectType;
import lenicorp.project.model.dtos.validator.ValidStatusType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@NotNull(message = "Aucune donnée parvenue")
public class UpdateProjectDTO
{
    @NotNull(message = "L'identifiant du projet est obligatoire")
    private Long projectId;

    @NotNull(message = "Le nom du projet est obligatoire")
    @NotBlank(message = "Le nom du projet est obligatoire")
    private String nomProjet;

    @NotNull(message = "La description du projet est obligatoire")
    @NotBlank(message = "La description du projet est obligatoire")
    private String description;

    @NotNull(message = "Le type de projet est obligatoire")
    @ValidProjectType
    private String typeProjetCode;

    private LocalDate debutProjet;
    private LocalDate finProjet;

    private LocalDate debutPrelevement;
    private Integer dureePrelevementsMois;

    @ValidFrequencyType
    private String frequencePrelevementCode;

    private String localisation;

    // Fields specific to land sales projects (Vente de terrain)
    private BigDecimal superficieTotale;

    // Fields specific to repayable loan projects (Prêt remboursable)
    private BigDecimal montantTotalPret;
    private BigDecimal tauxInteret;

    @ValidStatusType
    private String statutProjetCode;
}
