package lenicorp.paiement.model.validators;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lenicorp.paiement.controller.repositories.spec.IEcheancierRepo;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {ExistingEcheancierId.ExistingAssIdValidator.class})
public @interface ExistingEcheancierId
{
    String message() default "Echeancier introuvable";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @ApplicationScoped @RequiredArgsConstructor
    class ExistingAssIdValidator implements ConstraintValidator<ExistingEcheancierId, Long>
    {
        private final IEcheancierRepo echeancierRepo;
        @Override
        public boolean isValid(Long echeancierId, ConstraintValidatorContext context)
        {
            return echeancierRepo.existsById(echeancierId);
        }
    }
}
