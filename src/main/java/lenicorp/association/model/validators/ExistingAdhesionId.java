package lenicorp.association.model.validators;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lenicorp.association.controller.repositories.spec.IAdhesionRepo;
import lombok.RequiredArgsConstructor;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ExistingAdhesionId.ExistingAdhesionIdValidator.class})
@Documented
public @interface ExistingAdhesionId
{
    String message() default "L'ID du membre est introuvable";
    Class<?> [] groups() default {};
    Class<? extends Payload> [] payload() default {};


    @ApplicationScoped @RequiredArgsConstructor
    class ExistingAdhesionIdValidator implements ConstraintValidator<ExistingAdhesionId, Long>
    {
        private final IAdhesionRepo adhesionRepo;

        @Override
        public boolean isValid(Long value, ConstraintValidatorContext context)
        {
            if(value == null) return true;
            return adhesionRepo.existsById(value) ;
        }
    }
}