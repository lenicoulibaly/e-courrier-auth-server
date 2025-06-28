package lenicorp.security.model.dtos;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class UserDTO
{
    private Long userId;
    @Column(unique = true, nullable = false)
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String rePassword;
    private String oldPassword;
    private String resetPasswordToken;
    private LocalDate changePasswordDate;
    private boolean activated = false;
    private LocalDateTime lastLogin;
    private Long strId;
    private String strName;
    private String strSigle;
    private String chaineSigles;
}