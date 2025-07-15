package lenicorp.types.model.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lenicorp.types.model.validators.ExistingGroupCode;
import lenicorp.types.model.validators.ExistingTypeCode;
import lenicorp.types.model.validators.UniqueTypeName;
import lenicorp.utilities.validatorgroups.CreateGroup;
import lenicorp.utilities.validatorgroups.SetSousTypeGroup;
import lenicorp.utilities.validatorgroups.UpdateGroup;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@NotNull(message = "Aucune donnée parvenue")
@UniqueTypeName(groups = {UpdateGroup.class})
public class TypeDTO
{
    @NotNull(message = "Le code est obligatoire")
    @NotBlank(message = "Le code est obligatoire")
    @ExistingTypeCode(groups = {UpdateGroup.class, SetSousTypeGroup.class})
    private String code;
    @NotNull(message = "Le nom est obligatoire", groups = {CreateGroup.class, UpdateGroup.class})
    @NotBlank(message = "Le nom est obligatoire", groups = {CreateGroup.class, UpdateGroup.class})
    @UniqueTypeName(groups = {CreateGroup.class})
    private String name;
    private int ordre;
    @NotNull(message = "Le code du groupe est obligatoire" , groups = {CreateGroup.class, UpdateGroup.class})
    @NotBlank(message = "Le code du groupe est obligatoire", groups = {CreateGroup.class, UpdateGroup.class})
    @ExistingGroupCode(groups = {CreateGroup.class, UpdateGroup.class})
    private String groupCode;
    private String description;

    public TypeDTO(String code, String name, int ordre, String groupCode, String description) {
        this.code = code;
        this.name = name;
        this.ordre = ordre;
        this.groupCode = groupCode;
        this.description = description;
    }

    private List<TypeDTO> sousTypes = new ArrayList<>();
    private List<String> sousTypeCodes = new ArrayList<>();
}
