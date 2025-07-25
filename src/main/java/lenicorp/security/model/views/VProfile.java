package lenicorp.security.model.views;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Table(name = "v_profile")
public class VProfile
{
    @Id
    private String code;
    private String name;
    private String typeCode;
}