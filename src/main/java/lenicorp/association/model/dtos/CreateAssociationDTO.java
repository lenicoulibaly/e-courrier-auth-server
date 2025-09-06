package lenicorp.association.model.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.FormParam;
import lenicorp.association.model.validators.UniqueAssoName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@NotNull(message = "Aucune donnée parvenue")
public class CreateAssociationDTO
{
    @NotNull(message = "Le nom de l'association est obligatoire")
    @NotBlank(message = "Le nom de l'association est obligatoire")
    @UniqueAssoName
    @FormParam("assoName")
    private String assoName;
    @FormParam("situationGeo")
    private String situationGeo;
    @FormParam("sigle")
    private String sigle;
    @FormParam("droitAdhesion")
    private BigDecimal droitAdhesion;
    @FormParam("logo")
    private InputStream logo;
    //@Valid
    private List<CreateSectionDTO> createSectionDTOS;
}