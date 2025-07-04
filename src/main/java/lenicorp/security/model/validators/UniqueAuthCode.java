package lenicorp.security.model.validators;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lenicorp.security.controller.repositories.spec.IAuthorityRepo;
import lombok.RequiredArgsConstructor;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {UniqueAuthCode.UniqueAuthCodeValidator.class})
@Documented
public @interface UniqueAuthCode
{
    String message() default "Code déjà utilisé";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    boolean allowNull() default true;

    @ApplicationScoped @RequiredArgsConstructor
    class UniqueAuthCodeValidator implements ConstraintValidator<UniqueAuthCode, String>
    {
        private final IAuthorityRepo authorityRepo;
        private boolean allowNull;

        @Override
        public void initialize(UniqueAuthCode constraintAnnotation)
        {
            this.allowNull = constraintAnnotation.allowNull();
        }

        @Override
        public boolean isValid(String authCode, ConstraintValidatorContext context)
        {
            if (authCode == null) return allowNull;
            return !authorityRepo.existsByCode(authCode);
        }
    }
}