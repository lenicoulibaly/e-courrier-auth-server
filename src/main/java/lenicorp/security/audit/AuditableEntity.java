
package lenicorp.security.audit;

import jakarta.enterprise.inject.spi.CDI;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @MappedSuperclass @Audited
public class AuditableEntity
{
    protected String actionName;
    protected String actionId;
    protected String connexionId;
    @Column(name = "created_at")
    protected LocalDateTime createdAt;
    @Column(name = "created_by", length = 50)
    protected String createdBy;
    @Column(name = "updated_at")
    protected LocalDateTime updatedAt;
    @Column(name = "updated_by", length = 50)
    protected String updatedBy;

    @PrePersist
    protected void onCreate()
    {
        this.createdAt = LocalDateTime.now();
        this.createdBy = getCurrentUser();
    }

    @PreUpdate
    protected void onUpdate()
    {
        this.updatedAt = LocalDateTime.now();
        this.updatedBy = getCurrentUser();
    }

    public AuditableEntity(String actionName, String actionId, String connexionId)
    {
        this.actionName = actionName;
        this.actionId = actionId;
        this.connexionId = connexionId;
    }

    private String getCurrentUser()
    {
        try
        {
            var auditService = CDI.current().select(AuditService.class);
            if (!auditService.isUnsatisfied())
            {
                return auditService.get().getCurrentUsername();
            }
        }
        catch (Exception e)
        {
            // Log l'erreur si nécessaire
            System.err.println("Erreur lors de la récupération de l'utilisateur : " + e.getMessage());
        }
        return "SYSTEM";
    }
}