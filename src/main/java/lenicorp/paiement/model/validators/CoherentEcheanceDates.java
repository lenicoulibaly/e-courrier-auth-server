package lenicorp.paiement.model.validators;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lenicorp.paiement.model.dtos.CreateEcheanceDTO;
import lenicorp.paiement.model.dtos.UpdateEcheanceDTO;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {CoherentEcheanceDates.CoherentDatesValidatorOnCreate.class,
        CoherentEcheanceDates.CoherentDatesValidatorOnUpdate.class})
public @interface CoherentEcheanceDates
{
    String message() default "La date échéance ne peut être ultérieure à la date buttoire";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @ApplicationScoped @RequiredArgsConstructor
    class CoherentDatesValidatorOnCreate implements ConstraintValidator<CoherentEcheanceDates, CreateEcheanceDTO>
    {
        @Override
        public boolean isValid(CreateEcheanceDTO dto, ConstraintValidatorContext context)
        {
            return dto.getDateEcheance() == null || dto.getDateButtoire() == null ? true : dto.getDateEcheance().isBefore(dto.getDateButtoire()) || dto.getDateEcheance().isEqual(dto.getDateButtoire());
        }
    }

    @ApplicationScoped @RequiredArgsConstructor
    class CoherentDatesValidatorOnUpdate implements ConstraintValidator<CoherentEcheanceDates, UpdateEcheanceDTO>
    {
        @Override
        public boolean isValid(UpdateEcheanceDTO dto, ConstraintValidatorContext context)
        {
            return dto.getDateEcheance() == null || dto.getDateButtoire() == null ? true : dto.getDateEcheance().isBefore(dto.getDateButtoire()) || dto.getDateEcheance().isEqual(dto.getDateButtoire());
        }
    }
}
