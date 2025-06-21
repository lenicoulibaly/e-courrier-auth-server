package lenicorp.types.model.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lenicorp.types.model.validators.CreateGroup;
import lenicorp.types.model.validators.ExistingGroupCode;
import lenicorp.types.model.validators.UniqueGroupCode;
import lenicorp.types.model.validators.UpdateGroup;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class TypeGroupDTO
{
    @NotNull(message = "Le code du groupe ne peut être null")
    @NotBlank(message = "Le code du groupe ne peut être null")
    @UniqueGroupCode(groups = {CreateGroup.class})
    @ExistingGroupCode(groups = {UpdateGroup.class})
    private String groupCode;
    @NotNull(message = "Le nom du groupe ne peut être null")
    @NotBlank(message = "Le nom du groupe ne peut être null")
    private String name;

    public TypeGroupDTO(String groupCode) {
        this.groupCode = groupCode;
    }
}
