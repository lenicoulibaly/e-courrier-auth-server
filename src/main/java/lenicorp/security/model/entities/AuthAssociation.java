package lenicorp.security.model.entities;

import jakarta.persistence.*;
import lenicorp.types.model.entities.Type;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "auth_association")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthAssociation
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "type_code")
    private Type type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private AppUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_code")
    private AppAuthority profile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_code")
    private AppAuthority role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "privilege_code")
    private AppAuthority privilege;

    private Boolean active = false;
    private Boolean notBlocked = true;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}