package lenicorp.security.model.entities;

import jakarta.persistence.*;
import lenicorp.security.audit.AuditableEntity;
import lenicorp.types.model.entities.Type;
import lombok.*;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity @Table(name = "app_authority")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Audited
public class AppAuthority extends AuditableEntity
{
    @Id
    private String code;
    @Column(unique = true, nullable = false)
    private String name;
    private String description;
    @ManyToOne @JoinColumn(name = "type_code") @NotAudited
    private Type type;
    @ManyToOne @JoinColumn(name = "privilege_type_code") @NotAudited
    private Type privilegeType;

    public AppAuthority(String code)
    {
        this.code = code;
    }
}