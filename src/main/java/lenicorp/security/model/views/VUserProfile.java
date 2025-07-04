package lenicorp.security.model.views;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "v_user_profile")
public class VUserProfile
{
    @Id
    private Long rowNum;
    private Long userId;
    private String email;
    private String firstName;
    private String lastName;
    private Boolean activated;
    private Long strId;
    private String tel;
    private String profileCode;
    private String profileName;
    private String profileTypeCode;
    private String profileTypeName;
    private String assStatusCode;
    private String profileDescription;
}