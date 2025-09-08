package lenicorp.paiement.model.validators;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lenicorp.paiement.model.dtos.CreateEcheanceDTO;
import lenicorp.paiement.model.dtos.UpdateEcheanceDTO;
import lombok.RequiredArgsConstructor;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {MontantMax.MontantMaxValidatorOnCreate.class,
        MontantMax.MontantMaxValidatorOnUpdate.class})
public @interface MontantMax
{
    String message() default "Le montant de l'échéance ne peut pas dépasser la valeur maximale autorisée";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    double value() default 1000000.0;

    @ApplicationScoped @RequiredArgsConstructor
    class MontantMaxValidatorOnCreate implements ConstraintValidator<MontantMax, CreateEcheanceDTO>
    {
        private double maxValue;

        @Override
        public void initialize(MontantMax constraintAnnotation) {
            this.maxValue = constraintAnnotation.value();
        }

        @Override
        public boolean isValid(CreateEcheanceDTO dto, ConstraintValidatorContext context)
        {
            if (dto.getMontantEcheance() == null) return true;
            return dto.getMontantEcheance().doubleValue() <= maxValue;
        }
    }

    @ApplicationScoped @RequiredArgsConstructor
    class MontantMaxValidatorOnUpdate implements ConstraintValidator<MontantMax, UpdateEcheanceDTO>
    {
        private double maxValue;

        @Override
        public void initialize(MontantMax constraintAnnotation) {
            this.maxValue = constraintAnnotation.value();
        }

        @Override
        public boolean isValid(UpdateEcheanceDTO dto, ConstraintValidatorContext context)
        {
            if (dto.getMontantEcheance() == null) return true;
            return dto.getMontantEcheance().doubleValue() <= maxValue;
        }
    }
}
