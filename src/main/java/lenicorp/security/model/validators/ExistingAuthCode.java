package lenicorp.security.model.validators;

import jakarta.enterprise.inject.spi.CDI;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lenicorp.security.controller.repositories.spec.IAuthorityRepo;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ExistingAuthCode.ExistingAuthCodeValidator.class})
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


    class ExistingAuthCodeValidator implements ConstraintValidator<ExistingAuthCode, String>
    {
        //private final IAuthorityRepo authorityRepo;
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
            //System.out.println("DEBUG: Validation avec authCode=" + authCode + " et authType=" + authType);
            if (authCode == null) return allowNull;
            IAuthorityRepo authorityRepo = CDI.current().select(IAuthorityRepo.class).get();
            return authorityRepo.existsByCodeAndType(authCode, authType);
        }
    }
}