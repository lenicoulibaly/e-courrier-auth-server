package lenicorp.association.model.dtos;

import lenicorp.structures.model.validators.ExistingStrId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class UpdateSectionDTO
{
    private Long sectionId;
    private String sectionName;
    private String situationGeo;
    private String sigle;
    @ExistingStrId
    private Long strId;
}
