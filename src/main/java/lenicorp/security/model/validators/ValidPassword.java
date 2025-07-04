package lenicorp.security.model.validators;

import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lenicorp.security.controller.repositories.spec.IUserRepo;
import lenicorp.security.model.dtos.UserDTO;
import lombok.RequiredArgsConstructor;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ValidPassword.ValidPasswordValidator.class})
@Documented
public @interface ValidPassword
{
    String message() default "Mot de passe ou username incorrect";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    boolean allowNull() default true;

    @ApplicationScoped @RequiredArgsConstructor
    class ValidPasswordValidator implements ConstraintValidator<ValidPassword, UserDTO>
    {
        private final IUserRepo userRepo;

        @Override
        public boolean isValid(UserDTO dto, ConstraintValidatorContext context)
        {
            if (dto == null) return true;
            String password = dto.getPassword();
            if (password == null) return true;
            String cryptedStoredPassword = userRepo.getPasswordByUsername(dto.getEmail());
            if( cryptedStoredPassword == null || cryptedStoredPassword.isBlank()) return true;
            return BcryptUtil.matches(password, cryptedStoredPassword);
        }
    }
}