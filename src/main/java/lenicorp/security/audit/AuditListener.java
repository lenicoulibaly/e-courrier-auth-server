package lenicorp.security.audit;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.time.LocalDateTime;

public class AuditListener 
{
    @PrePersist
    public void setCreatedInfo(AuditableEntity entity) {
        entity.setCreatedAt(LocalDateTime.now());
        entity.setCreatedBy(getCurrentUser());
    }

    @PreUpdate
    public void setUpdatedInfo(AuditableEntity entity) {
        entity.setUpdatedAt(LocalDateTime.now());
        entity.setUpdatedBy(getCurrentUser());
    }

    private String getCurrentUser() {
        // Logique pour récupérer l'utilisateur courant
        // Pour l'instant, retourner une valeur par défaut
        return "SYSTEM";
    }
}