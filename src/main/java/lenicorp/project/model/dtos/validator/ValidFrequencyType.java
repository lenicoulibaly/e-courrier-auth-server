package lenicorp.project.model.dtos.validator;

import jakarta.inject.Inject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lenicorp.types.controller.repositories.TypeRepo;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ValidFrequencyType.ValidFrequencyTypeValidator.class})
@Documented
public @interface ValidFrequencyType
{
    String message() default "Fréquence de prélèvement invalide";
    Class<?> [] groups() default {};
    Class<? extends Payload> [] payload() default {};

    @ApplicationScoped
    class ValidFrequencyTypeValidator implements ConstraintValidator<ValidFrequencyType, String>
    {
        @Inject
        TypeRepo typeRepo;

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context)
        {
            if(value == null) return true;
            return typeRepo.existsByCodeAndGroupCode(value, "FREQUENCY");
        }
    }
}