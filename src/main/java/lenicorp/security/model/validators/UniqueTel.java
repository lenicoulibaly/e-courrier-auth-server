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
@Constraint(validatedBy = {UniqueTel.UniqueTelValidatorForCreate.class, UniqueTel.UniqueTelValidatorForUpdate.class})
@Documented
public @interface UniqueTel
{
    String message() default "Le numéro de téléphone '{validatedValue}' existe déjà";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    boolean allowNull() default false;

    @ApplicationScoped @RequiredArgsConstructor
    class UniqueTelValidatorForCreate implements ConstraintValidator<UniqueTel, String>
    {
        private final IUserRepo userRepo;
        private boolean allowNull;

        @Override
        public void initialize(UniqueTel constraintAnnotation)
        {
            this.allowNull = constraintAnnotation.allowNull();
        }

        @Override
        public boolean isValid(String tel, ConstraintValidatorContext context)
        {
            if (StringUtils.isBlank(tel)) return allowNull;
            return !userRepo.existsByTel(tel.toUpperCase());
        }
    }

    @ApplicationScoped @RequiredArgsConstructor
    class UniqueTelValidatorForUpdate implements ConstraintValidator<UniqueTel, UserDTO>
    {
        private final IUserRepo userRepo;
        private boolean allowNull;

        @Override
        public void initialize(UniqueTel constraintAnnotation)
        {
            this.allowNull = constraintAnnotation.allowNull();
        }

        @Override
        public boolean isValid(UserDTO dto, ConstraintValidatorContext context)
        {
            if (dto == null) return true;
            String tel = dto.getTel();
            if (StringUtils.isBlank(tel)) return allowNull;
            return !userRepo.existsByTel(tel.toUpperCase(), dto.getUserId());
        }
    }
}