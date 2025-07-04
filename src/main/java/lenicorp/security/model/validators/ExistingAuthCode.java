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
@Constraint(validatedBy = {ExistingAuthCode.UniqueAuthCodeValidator.class})
@Documented
@Repeatable(ExistingAuthCode.List.class) // Permet la répétition
public @interface ExistingAuthCode
{
    String message() default "Code inconnu";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    boolean allowNull() default true;
    String authType();

    // Annotation conteneur pour permettre la répétition
    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        ExistingAuthCode[] value();
    }

    @ApplicationScoped @RequiredArgsConstructor
    class UniqueAuthCodeValidator implements ConstraintValidator<ExistingAuthCode, String>
    {
        private final IAuthorityRepo authorityRepo;
        private boolean allowNull;
        private String authType;

        @Override
        public void initialize(ExistingAuthCode constraintAnnotation)
        {
            this.allowNull = constraintAnnotation.allowNull();
            this.authType = constraintAnnotation.authType();
        }

        @Override
        public boolean isValid(String authCode, ConstraintValidatorContext context)
        {
            if (authCode == null) return allowNull;
            return authorityRepo.existsByCodeAndType(authCode, authType);
        }
    }
}