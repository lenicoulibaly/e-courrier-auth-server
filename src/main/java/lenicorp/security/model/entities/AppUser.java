package lenicorp.security.model.entities;

import jakarta.persistence.*;
import lenicorp.security.audit.AuditableEntity;
import lenicorp.structures.model.entities.Structure;
import lombok.*;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "app_user")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Audited
public class AppUser extends AuditableEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "APP_USER_ID_GEN")
    @SequenceGenerator(name = "APP_USER_ID_GEN", sequenceName = "APP_USER_ID_GEN", initialValue = 1)
    private Long userId;

    @Column(unique = true, nullable = false)
    private String email;

    private String firstName;
    private String lastName;

    @Column(nullable = true) @NotAudited
    private String password;
    private LocalDate changePasswordDate;

    @Column(nullable = false)
    private boolean activated = false;

    private LocalDateTime lastLogin;
    @ManyToOne @JoinColumn(name = "str_id")
    private Structure structure;
}