package lenicorp.association.model.validators;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lenicorp.association.controller.repositories.spec.ISectionRepo;
import lombok.RequiredArgsConstructor;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ExistingSectionId.ExistingSectionIdValidator.class})
@Documented
public @interface ExistingSectionId
{
    String message() default "L'ID de la section est introuvable";
    Class<?> [] groups() default {};
    Class<? extends Payload> [] payload() default {};

    @ApplicationScoped @RequiredArgsConstructor
    class ExistingSectionIdValidator implements ConstraintValidator<ExistingSectionId, Long>
    {
        private final ISectionRepo sectionRepo;

        @Override
        public boolean isValid(Long value, ConstraintValidatorContext context)
        {
            if(value == null) return true;
            return sectionRepo.existsById(value) ;
        }
    }
}