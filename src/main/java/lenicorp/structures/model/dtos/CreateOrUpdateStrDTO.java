package lenicorp.structures.model.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lenicorp.structures.model.validators.CompatibleTypeAndStrParent;
import lenicorp.structures.model.validators.ExistingStrId;
import lenicorp.structures.model.validators.UniqueSigleUnderSameParent;
import lenicorp.structures.model.validators.UniqueStrNameUnderSameParent;
import lenicorp.types.model.validators.CreateGroup;
import lenicorp.types.model.validators.ExistingTypeCode;
import lenicorp.types.model.validators.UpdateGroup;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor //@Entity
@CompatibleTypeAndStrParent
@UniqueSigleUnderSameParent(groups = {CreateGroup.class, UpdateGroup.class})
@UniqueStrNameUnderSameParent(groups = {CreateGroup.class, UpdateGroup.class})
@NotNull(message = "Aucune donnée parvenue")
public class CreateOrUpdateStrDTO
{
    @NotNull(groups = {UpdateGroup.class}, message = "L'ID est obligatoire")
    @Null(groups = {CreateGroup.class}, message = "L'ID de la structure doit être nul")
    @ExistingStrId(groups = {UpdateGroup.class})
    private Long strId;
    @Length(message = "Le nom de la structure doit contenir au moins 3 caractères", min = 3)
    @NotNull(message = "Le nom de la structure ne peut être nul")
    private String strName;
    @NotNull(message = "Le sigle de la structure ne peut être nul")
    private String strSigle;
    @NotNull(message = "Le type de la structure ne peut être nul")
    @ExistingTypeCode
    private String typeCode;
    @ExistingStrId(allowNull = true)
    private Long parentId;

    private String strTel;
    private String strAddress;
    @NotNull(message = "La situation géographique ne peut être nulle")
    private String situationGeo;

    @Override
    public String toString()
    {
        return this.strName + " ("+this.strSigle + ")";
    }

}
