package lenicorp.association.model.validators;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lenicorp.association.controller.repositories.spec.IAssoRepo;
import lombok.RequiredArgsConstructor;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ExistingAssoId.ExistingAssoIdValidator.class})
@Documented
public @interface ExistingAssoId
{
    String message() default "L'ID de l'association est introuvable";
    Class<?> [] groups() default {};
    Class<? extends Payload> [] payload() default {};

    @ApplicationScoped @RequiredArgsConstructor
    class ExistingAssoIdValidator implements ConstraintValidator<ExistingAssoId, Long>
    {
        private final IAssoRepo assoRepo;

        @Override
        public boolean isValid(Long value, ConstraintValidatorContext context)
        {
            if(value == null) return true;
            return assoRepo.existsById(value) ;
        }
    }
}