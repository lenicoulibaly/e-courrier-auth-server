package lenicorp.security.model.dtos;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lenicorp.security.model.validators.*;
import lenicorp.structures.model.validators.ExistingStrId;
import lenicorp.utilities.validatorgroups.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data @AllArgsConstructor @NoArgsConstructor
@UniqueEmail(message = "L'email est déjà utilisé", groups = {UpdateGroup.class})
@UniqueTel(message = "Le numéro de téléphone est déjà utilisé", groups = {UpdateGroup.class})
@SamePasswordAndRepassword(message = "Les mots de passe ne correspondent pas", groups = {ChangePasswordGroup.class, ActivateAccountGroup.class, ResetPasswordGroup.class})
@ValidToken(message = "Le token de réinitialisation du mot de passe est invalide", groups = {ActivateAccountGroup.class, ResetPasswordGroup.class})
@NotNull(message = "L'utilisateur ne peut pas être nul")

@ValidPassword(groups = {LoginGroup.class})
@ValidOldPassword(groups = {ChangePasswordGroup.class})
public class UserDTO
{
    @NotNull(message = "L'identifiant de l'utilisateur ne peut pas être nul", groups = {UpdateGroup.class, ChangePasswordGroup.class, ActivateAccountGroup.class, ResetPasswordGroup.class})
    @ExistingUserId(message = "L'utilisateur n'existe pas", allowNull = true, groups = {UpdateGroup.class, ChangePasswordGroup.class, ActivateAccountGroup.class, ResetPasswordGroup.class})
    @ActiveUser(groups = {ChangePasswordGroup.class, ResetPasswordGroup.class})
    @NotBlockedUser(groups = {ChangePasswordGroup.class, ResetPasswordGroup.class})
    private Long userId;
    @Column(unique = true, nullable = false)
    @NotNull(message = "L'email de l'utilisateur ne peut pas être nul", groups = {CreateGroup.class, LoginGroup.class, SendResetPasswordEmailGroup.class})
    @UniqueEmail(message = "L'email est déjà utilisé", groups = {CreateGroup.class})
    @Email(message = "L'email est invalide", groups = {CreateGroup.class, UpdateGroup.class})
    @ActiveUser(groups = {LoginGroup.class})
    @NotBlockedUser(groups = {LoginGroup.class})
    @ExistingEmail(allowNull = true, groups = {LoginGroup.class, SendResetPasswordEmailGroup.class})
    private String email;
    @NotNull(message = "Le prénom ne peut pas être nul")
    private String firstName;
    @NotNull(message = "Le nom de famille ne peut pas être nul")
    private String lastName;
    @NotNull(message = "Le numéro de téléphone ne peut pas être nul", groups = {CreateGroup.class, UpdateGroup.class})
    @UniqueTel(message = "Le numéro de téléphone est déjà utilisé", groups = {CreateGroup.class})
    private String tel;
    @NotNull(message = "Le mot de passe ne peut pas être nul", groups = {ChangePasswordGroup.class, ActivateAccountGroup.class, ResetPasswordGroup.class, LoginGroup.class})
    private String password;
    @NotNull(message = "La confirmation du mot de passe ne peut pas être nulle", groups = {ChangePasswordGroup.class, ActivateAccountGroup.class, ResetPasswordGroup.class})
    private String rePassword;
    @NotNull(message = "L'ancien mot de passe ne peut pas être nul", groups = {ChangePasswordGroup.class})
    private String oldPassword;
    @NotNull(message = "Aucun token fourni", groups = {ActivateAccountGroup.class, ResetPasswordGroup.class})
    @NotExpiredToken(message = "Lien expiré", groups = {ActivateAccountGroup.class, ResetPasswordGroup.class})
    @NotAlreadyUsedToken(message = "Lien déjà utilisé", groups = {ActivateAccountGroup.class, ResetPasswordGroup.class})
    private String authToken;
    private LocalDate changePasswordDate;
    private boolean activated = false;
    private boolean notBlocked = true;
    private LocalDateTime lastLogin;
    @ExistingStrId(allowNull = true, groups = {CreateGroup.class, UpdateGroup.class})
    private Long strId;
    private String strName;
    private String strSigle;
    private String chaineSigles;

    public UserDTO(Long userId, String email, String firstName, String lastName, String tel, LocalDate changePasswordDate, boolean activated, boolean notBlocked, LocalDateTime lastLogin, Long strId, String strName, String strSigle, String chaineSigles)
    {
        this.userId = userId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.tel = tel;
        this.changePasswordDate = changePasswordDate;
        this.activated = activated;
        this.notBlocked = notBlocked;
        this.lastLogin = lastLogin;
        this.strId = strId;
        this.strName = strName;
        this.strSigle = strSigle;
        this.chaineSigles = chaineSigles;
    }
}