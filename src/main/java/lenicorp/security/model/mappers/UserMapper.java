
package lenicorp.security.model.mappers;

import lenicorp.security.model.dtos.CreateUserDTO;
import lenicorp.security.model.dtos.UserDTO;
import lenicorp.security.model.dtos.UserProfileAssoDTO;
import lenicorp.security.model.entities.AppUser;
import lenicorp.structures.controller.repositories.VStrRepo;
import lenicorp.structures.model.entities.Structure;
import org.mapstruct.*;
import jakarta.inject.Inject;

@Mapper(componentModel = "cdi")
public abstract class UserMapper
{
    @Inject
    protected VStrRepo vStrRepo;

    /**
     * Mapping de UserDTO vers AppUser pour création
     */
    @Mapping(target = "userId", ignore = true) // L'ID sera généré automatiquement
    @Mapping(target = "structure", source = "strId", qualifiedByName = "mapStructureId")
    @Mapping(target = "createdAt", ignore = true) // Géré par AuditableEntity
    @Mapping(target = "createdBy", ignore = true) // Géré par AuditableEntity
    @Mapping(target = "updatedAt", ignore = true) // Géré par AuditableEntity
    @Mapping(target = "updatedBy", ignore = true) // Géré par AuditableEntity
    @Mapping(target = "actionName", ignore = true) // Géré par AuditableEntity
    @Mapping(target = "actionId", ignore = true) // Géré par AuditableEntity
    @Mapping(target = "connexionId", ignore = true)
    @Mapping(target = "password", ignore = true) // Ne pas mapper le champ de confirmation
    // Géré par AuditableEntity
    public abstract AppUser mapToAppUser(UserDTO userDTO);

    /**
     * Mapping de UserDTO vers AppUser existant pour mise à jour
     */
    @Mapping(target = "structure", source = "strId", qualifiedByName = "mapStructureId")
    @Mapping(target = "createdAt", ignore = true) // Ne pas modifier lors de la mise à jour
    @Mapping(target = "createdBy", ignore = true) // Ne pas modifier lors de la mise à jour
    @Mapping(target = "updatedAt", ignore = true) // Géré par AuditableEntity
    @Mapping(target = "updatedBy", ignore = true) // Géré par AuditableEntity
    @Mapping(target = "actionName", ignore = true) // Géré par AuditableEntity
    @Mapping(target = "actionId", ignore = true) // Géré par AuditableEntity
    @Mapping(target = "connexionId", ignore = true)
    @Mapping(target = "password", ignore = true)
    // Géré par AuditableEntity
    public abstract AppUser updateUser(UserDTO userDTO, @MappingTarget AppUser appUser);

    /**
     * Mapping de AppUser vers UserDTO
     */
    @Mapping(target = "strId", source = "structure.strId")
    @Mapping(target = "strName", source = "structure.strName")
    @Mapping(target = "strSigle", source = "structure.strSigle")
    @Mapping(target = "chaineSigles", source = "structure.strId", qualifiedByName = "mapChaineSigles")
    @Mapping(target = "rePassword", ignore = true) // Ne pas mapper le champ de confirmation
    @Mapping(target = "oldPassword", ignore = true) // Ne pas mapper l'ancien mot de passe
    @Mapping(target = "authToken", ignore = true) // Ne pas mapper le token de reset
    @Mapping(target = "password", ignore = true)
    // Pour des raisons de sécurité, ne pas exposer le mot de passe
    public abstract UserDTO mapToUserDTO(AppUser appUser);

    /**
     * Méthode personnalisée pour mapper l'ID de structure vers l'entité Structure
     */
    @Named("mapStructureId")
    protected Structure mapStructureId(Long strId)
    {
        if (strId == null)
        {
            return null;
        }
        return new Structure(strId);
    }

    /**
     * Méthode personnalisée pour récupérer la chaîne des sigles depuis VStructure
     */
    @Named("mapChaineSigles")
    protected String mapChaineSigles(Long strId)
    {
        if (strId == null) {
            return null;
        }
        return vStrRepo.getChaineSigles(strId);
    }

    /**
     * Mapping de CreateUserDTO vers AppUser pour création
     */
    @Mapping(target = "userId", ignore = true) // L'ID sera généré automatiquement
    @Mapping(target = "structure", source = "strId", qualifiedByName = "mapStructureId")
    @Mapping(target = "createdAt", ignore = true) // Géré par AuditableEntity
    @Mapping(target = "createdBy", ignore = true) // Géré par AuditableEntity
    @Mapping(target = "updatedAt", ignore = true) // Géré par AuditableEntity
    @Mapping(target = "updatedBy", ignore = true) // Géré par AuditableEntity
    @Mapping(target = "actionName", ignore = true) // Géré par AuditableEntity
    @Mapping(target = "actionId", ignore = true) // Géré par AuditableEntity
    @Mapping(target = "connexionId", ignore = true)
    @Mapping(target = "password", ignore = true) // Le mot de passe sera généré lors de l'activation
    @Mapping(target = "activated", constant = "false") // L'utilisateur n'est pas activé par défaut
    @Mapping(target = "notBlocked", constant = "true") // L'utilisateur n'est pas bloqué par défaut
    @Mapping(target = "lastLogin", ignore = true)
    @Mapping(target = "changePasswordDate", ignore = true)
    public abstract AppUser mapToAppUser(CreateUserDTO createUserDTO);

    /**
     * Mapping de CreateUserDTO vers UserProfileAssoDTO
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "libelle", ignore = true)
    @Mapping(target = "typeCode", ignore = true)
    @Mapping(target = "typeName", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "profileName", ignore = true)
    @Mapping(target = "strName", ignore = true)
    @Mapping(target = "userProfileAssTypeCode", source = "userProfileAssTypeCode")
    public abstract UserProfileAssoDTO mapToUserProfileAssoDTO(CreateUserDTO createUserDTO);
}
