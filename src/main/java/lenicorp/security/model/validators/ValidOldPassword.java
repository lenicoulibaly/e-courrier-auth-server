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
@Constraint(validatedBy = {ValidOldPassword.ValidOldPasswordValidator.class})
@Documented
public @interface ValidOldPassword
{
    String message() default "Ancien mot de passe ou username incorrect";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    boolean allowNull() default true;

    @ApplicationScoped @RequiredArgsConstructor
    class ValidOldPasswordValidator implements ConstraintValidator<ValidOldPassword, UserDTO>
    {
        private final IUserRepo userRepo;

        @Override
        public boolean isValid(UserDTO dto, ConstraintValidatorContext context)
        {
            if (dto == null) return true;
            String oldPassword = dto.getOldPassword();
            if (oldPassword == null) return true;
            String cryptedStoredPassword = userRepo.getPasswordByUsername(dto.getEmail());
            if( cryptedStoredPassword == null || cryptedStoredPassword.isBlank()) return true;
            return BcryptUtil.matches(oldPassword, cryptedStoredPassword);
        }
    }
}