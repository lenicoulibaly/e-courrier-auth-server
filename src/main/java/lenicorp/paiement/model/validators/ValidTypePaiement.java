package lenicorp.paiement.model.validators;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lenicorp.types.controller.repositories.spec.ITypeRepo;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ValidTypePaiement.ValidTypePaiementValidator.class})
@Documented
public @interface ValidTypePaiement
{
    String message() default "Type de paiement invalid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @ApplicationScoped @RequiredArgsConstructor
    class ValidTypePaiementValidator implements ConstraintValidator<ValidTypePaiement, String>
    {
        private final ITypeRepo typeRepo;
        @Override
        public boolean isValid(String typePaiement, ConstraintValidatorContext context) {
            if(typePaiement == null) return true;
            return typeRepo.existsByCodeAndGroupCode(typePaiement, "TYPE_PAIEMENT");
        }
    }
}
