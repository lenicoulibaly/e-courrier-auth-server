package lenicorp.project.model.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.FormParam;
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
public class CreateProjectDTO
{
    @NotNull(message = "Le nom du projet est obligatoire")
    @NotBlank(message = "Le nom du projet est obligatoire")
    @FormParam("nomProjet")
    private String nomProjet;

    @NotNull(message = "La description du projet est obligatoire")
    @NotBlank(message = "La description du projet est obligatoire")
    @FormParam("description")
    private String description;

    @NotNull(message = "Le type de projet est obligatoire")
    @ValidProjectType
    @FormParam("typeProjetCode")
    private String typeProjetCode;

    @FormParam("debutProjet")
    private LocalDate debutProjet;

    @FormParam("finProjet")
    private LocalDate finProjet;

    @FormParam("debutPrelevement")
    private LocalDate debutPrelevement;

    @FormParam("dureePrelevementsMois")
    private Integer dureePrelevementsMois;

    @ValidFrequencyType
    @FormParam("frequencePrelevementCode")
    private String frequencePrelevementCode;

    @FormParam("localisation")
    private String localisation;

    // Fields specific to land sales projects (Vente de terrain)
    @FormParam("superficieTotale")
    private BigDecimal superficieTotale;

    // Fields specific to repayable loan projects (Prêt remboursable)
    @FormParam("montantTotalPret")
    private BigDecimal montantTotalPret;

    @FormParam("tauxInteret")
    private BigDecimal tauxInteret;

    @ValidStatusType
    @FormParam("statutProjetCode")
    private String statutProjetCode;
}
