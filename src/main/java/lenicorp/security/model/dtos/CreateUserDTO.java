package lenicorp.security.model.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lenicorp.security.model.validators.CreateUserDateValidator;
import lenicorp.security.model.validators.ExistingAuthCode;
import lenicorp.security.model.validators.ProfileMaxAssignation;
import lenicorp.security.model.validators.UniqueEmail;
import lenicorp.security.model.validators.UniqueTel;
import lenicorp.structures.model.validators.ExistingStrId;
import lenicorp.types.model.validators.ExistingTypeCode;
import lenicorp.utilities.validatorgroups.CreateGroup;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO for creating new users
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@NotNull(message = "Les données de l'utilisateur ne peuvent pas être nulles")
@CreateUserDateValidator(groups = {CreateGroup.class})
@ProfileMaxAssignation(groups = {CreateGroup.class})
public class CreateUserDTO
{
    @NotNull(message = "L'email de l'utilisateur ne peut pas être nul", groups = {CreateGroup.class})
    @UniqueEmail(message = "L'email est déjà utilisé", groups = {CreateGroup.class})
    @Email(message = "L'email est invalide", groups = {CreateGroup.class})
    private String email;

    @NotNull(message = "Le prénom ne peut pas être nul")
    private String firstName;

    @NotNull(message = "Le nom de famille ne peut pas être nul")
    private String lastName;

    @NotNull(message = "Le numéro de téléphone ne peut pas être nul", groups = {CreateGroup.class})
    @UniqueTel(message = "Le numéro de téléphone est déjà utilisé", groups = {CreateGroup.class})
    private String tel;

    @ExistingStrId(allowNull = false, groups = {CreateGroup.class})
    @NotNull(message = "La structure est obligatoire")
    private Long strId; // Utiliser useVisibleStructure() pour la liste déroulante

    @ExistingAuthCode(authType = "PRFL")
    private String profileCode;

    @ExistingTypeCode(message = "Type d'assignation inconnu", groups = {CreateGroup.class}, typeGroupCode = "USR_PRFL_TYPE")
    private String userProfileAssTypeCode; // Utiliser useTypesByGroupCode("USR_PRFL_TYPE") pour la liste déroulante

    private LocalDate startingDate;
    private LocalDate endingDate;
}
