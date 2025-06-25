package lenicorp.structures.model.dtos;

import jakarta.validation.constraints.NotNull;
import lenicorp.structures.model.validators.CompatibleTypeAndStrParent;
import lenicorp.types.model.validators.ExistingTypeCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.Objects;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@CompatibleTypeAndStrParent
public class ChangeAnchorDTO
{
    @NotNull(message = "L'ID de la structure ne peut être nul")
    private Long strId;
    @ExistingTypeCode @NotNull(message = "L'ID du type ne peut être nul")
    private String newTypeCode;
    @NotNull(message = "L'ID de la structure de tutelle ne peut être nul")
    private Long newParentId;

    @Length(message = "Le nom de la structure doit contenir au moins 3 caractères", min = 3)
    @NotNull(message = "Le nom de la structure ne peut être nul")
    private String newStrName;

    @NotNull(message = "Le sigle de la structure ne peut être nul")
    private String newStrSigle;

    private long strLevel;

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof ChangeAnchorDTO)) return false;
        ChangeAnchorDTO that = (ChangeAnchorDTO) o;
        return Objects.equals(strId, that.strId) && Objects.equals(newTypeCode, that.newTypeCode) && Objects.equals(newParentId, that.newParentId)
                && Objects.equals(newStrName, that.newStrName) && Objects.equals(newStrSigle, that.newStrSigle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(strId, newTypeCode, newParentId, newStrName, newStrSigle);
    }
}
