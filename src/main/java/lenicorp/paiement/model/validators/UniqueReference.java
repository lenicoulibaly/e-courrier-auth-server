package lenicorp.paiement.model.validators;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lenicorp.paiement.controller.repositories.spec.IPaiementRepo;
import lenicorp.paiement.model.dtos.PaiementCotisationDTO;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {UniqueReference.UniqueReferenceValidator.class})
@Documented
public @interface UniqueReference
{
    String message() default "Référence paiement déjà utilisée";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @ApplicationScoped @RequiredArgsConstructor
    class UniqueReferenceValidator implements ConstraintValidator<UniqueReference, PaiementCotisationDTO>
    {
        private final IPaiementRepo paiementRepo;
        @Override
        public boolean isValid(PaiementCotisationDTO dto, ConstraintValidatorContext context)
        {
            if(dto == null) return true;
            if(dto.getPaiementId() == null) return !paiementRepo.existsByReference(dto.getReference());
            return !paiementRepo.existsByReference(dto.getReference(), dto.getPaiementId());
        }
    }
}
