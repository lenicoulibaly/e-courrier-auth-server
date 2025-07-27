package lenicorp.security.model.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lenicorp.security.model.validators.*;
import lenicorp.security.model.validators.ExistingAuthAssoId;
import lenicorp.structures.model.validators.ExistingStrId;
import lenicorp.types.model.validators.ExistingTypeCode;
import lenicorp.utilities.validatorgroups.CreateGroup;
import lenicorp.utilities.validatorgroups.UpdateGroup;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link lenicorp.security.model.entities.AuthAssociation}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@NotNull
@UniqueUserProfileAssociation(groups = {CreateGroup.class, UpdateGroup.class})
@ProfileMaxAssignation(groups = {CreateGroup.class, UpdateGroup.class})
@DateConsistencyValidator(groups = {CreateGroup.class, UpdateGroup.class})
@EndingDateRequiredValidator(groups = {CreateGroup.class, UpdateGroup.class})
public class UserProfileAssoDTO implements Serializable
{
    @ExistingAuthAssoId(message = "L'association n'existe pas ou n'est pas de type USR_PRFL", groups = {UpdateGroup.class})
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
    @ExistingTypeCode(message = "Type d'assignation inconnu", groups = {CreateGroup.class, UpdateGroup.class}, typeGroupCode = "USR_PRFL_TYPE")
    String userProfileAssTypeCode;
    String userProfileAssTypeName;
    LocalDate startingDate;
    LocalDate endingDate;
    String assStatusCode;
    String assStatusName;
    Long ordre;
    String firstName;
    String lastName;
}
