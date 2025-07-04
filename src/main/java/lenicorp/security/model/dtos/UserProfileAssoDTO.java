package lenicorp.security.model.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lenicorp.security.model.validators.ExistingAuthCode;
import lenicorp.security.model.validators.ExistingUserId;
import lenicorp.security.model.validators.UniqueUserProfileAssociation;
import lenicorp.structures.model.validators.ExistingStrId;
import lenicorp.utilities.validatorgroups.CreateGroup;
import lenicorp.utilities.validatorgroups.UpdateGroup;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO for {@link lenicorp.security.model.entities.AuthAssociation}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@NotNull
@UniqueUserProfileAssociation(groups = {CreateGroup.class, UpdateGroup.class})
public class UserProfileAssoDTO implements Serializable
{
    Long id;
    private String libelle;
    String typeCode;
    String typeName;
    @ExistingUserId
    @NotNull(message = "L'utilisateur est obligatoire")
    Long userId;
    String email;
    @ExistingAuthCode(authType = "PRFL")
    @NotNull(message = "Le profil est obligatoire")
    @NotBlank(message = "Le profil est obligatoire")
    String profileCode;
    String profileName;
    @ExistingStrId
    @NotNull(message = "La structure est obligatoire")
    Long strId;
    String strName;
}