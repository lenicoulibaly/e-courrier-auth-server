package lenicorp.types.model.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lenicorp.types.model.validators.CreateGroup;
import lenicorp.types.model.validators.ExistingGroupCode;
import lenicorp.types.model.validators.UniqueTypeName;
import lenicorp.types.model.validators.UpdateGroup;
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
    private String code;
    @NotNull(message = "Le nom est obligatoire") @NotNull(message = "Le nom est obligatoire")
    @UniqueTypeName(groups = {CreateGroup.class})
    private String name;
    private int ordre;
    @NotNull(message = "Le code du groupe est obligatoire")
    @NotNull(message = "Le code du groupe est obligatoire")
    @ExistingGroupCode
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
