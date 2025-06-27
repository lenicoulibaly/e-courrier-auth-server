package lenicorp.security.model.entities;

import jakarta.persistence.*;
import lenicorp.types.model.entities.Type;
import lombok.*;
import org.hibernate.envers.NotAudited;

@Entity
@Table(name = "app_authority")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppAuthority
{
    @Id
    private String code;
    @Column(unique = true, nullable = false)
    private String name;
    private String description;
    @ManyToOne @JoinColumn(name = "type_code") @NotAudited
    private Type type;
}