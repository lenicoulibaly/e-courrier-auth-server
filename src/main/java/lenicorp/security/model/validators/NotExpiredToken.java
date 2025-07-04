package lenicorp.security.model.validators;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lenicorp.security.controller.repositories.spec.IAuthTokenRepo;
import lombok.RequiredArgsConstructor;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotExpiredToken.Validator.class)
@Documented
public @interface NotExpiredToken
{
    String message() default "Token expiré";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    boolean allowNull() default true;

    @ApplicationScoped @RequiredArgsConstructor
    class Validator implements ConstraintValidator<NotExpiredToken, String>
    {
        private final IAuthTokenRepo authTokenRepo;
        private boolean allowNull;

        @Override
        public void initialize(NotExpiredToken constraintAnnotation)
        {
            this.allowNull = constraintAnnotation.allowNull();
        }

        @Override
        public boolean isValid(String token, ConstraintValidatorContext context)
        {
            if (token == null) return allowNull;

            return authTokenRepo.tokenHasNotExpired(token);
        }
    }
}