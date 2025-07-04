package lenicorp.security.model.validators;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lenicorp.security.controller.repositories.spec.IUserRepo;
import lenicorp.security.model.dtos.UserDTO;
import lenicorp.utilities.StringUtils;
import lombok.RequiredArgsConstructor;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {UniqueEmail.UniqueEmailValidatorForCreate.class, UniqueEmail.UniqueEmailValidatorForUpdate.class})
@Documented
public @interface UniqueEmail
{
    String message() default "L'adresse mail '{validatedValue}' existe déjà";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    boolean allowNull() default false;

    @ApplicationScoped @RequiredArgsConstructor
    class UniqueEmailValidatorForCreate implements ConstraintValidator<UniqueEmail, String>
    {
        private final IUserRepo userRepo;
        private boolean allowNull;

        @Override
        public void initialize(UniqueEmail constraintAnnotation)
        {
            this.allowNull = constraintAnnotation.allowNull();
        }

        @Override
        public boolean isValid(String email, ConstraintValidatorContext context)
        {
            if (StringUtils.isBlank(email)) return allowNull;
            return !userRepo.existsByEmail(email.toUpperCase());
        }
    }

    @ApplicationScoped @RequiredArgsConstructor
    class UniqueEmailValidatorForUpdate implements ConstraintValidator<UniqueEmail, UserDTO>
    {
        private final IUserRepo userRepo;
        private boolean allowNull;

        @Override
        public void initialize(UniqueEmail constraintAnnotation)
        {
            this.allowNull = constraintAnnotation.allowNull();
        }

        @Override
        public boolean isValid(UserDTO dto, ConstraintValidatorContext context)
        {
            if (dto == null) return true;
            String email = dto.getEmail();
            if (StringUtils.isBlank(email)) return allowNull;
            return !userRepo.existsByEmail(email.toUpperCase(), dto.getUserId());
        }
    }
}