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
@Constraint(validatedBy = {ValidModePaiement.ValidModePaiementValidator.class})
@Documented
public @interface ValidModePaiement
{
    String message() default "Mode de paiement invalid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @ApplicationScoped @RequiredArgsConstructor
    class ValidModePaiementValidator implements ConstraintValidator<ValidModePaiement, String>
    {
        private final ITypeRepo typeRepo;
        @Override
        public boolean isValid(String modePaiement, ConstraintValidatorContext context) {
            if(modePaiement == null) return true;
            return typeRepo.existsByCodeAndGroupCode(modePaiement, "MODE_PAIEMENT");
        }
    }
}
