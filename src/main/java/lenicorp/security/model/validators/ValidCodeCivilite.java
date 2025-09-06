package lenicorp.security.model.validators;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lenicorp.types.controller.repositories.TypeRepo;
import lombok.RequiredArgsConstructor;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidCodeCivilite.Validator.class)
@Documented
public @interface ValidCodeCivilite
{
    String message() default "Civilité inconnue";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    boolean allowNull() default true;

    @ApplicationScoped @RequiredArgsConstructor
    class Validator implements ConstraintValidator<ValidCodeCivilite, String>
    {
        private final TypeRepo typeRepo;
        private boolean allowNull;

        @Override
        public void initialize(ValidCodeCivilite constraintAnnotation)
        {
            this.allowNull = constraintAnnotation.allowNull();
        }

        @Override
        public boolean isValid(String codeCivilite, ConstraintValidatorContext context)
        {
            if (codeCivilite == null) return allowNull;
            return typeRepo.existsByCodeAndGroupCode(codeCivilite, "CIVILITE");
        }
    }
}