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
@Constraint(validatedBy = {ValidProjectType.ValidProjectTypeValidator.class})
@Documented
public @interface ValidProjectType
{
    String message() default "Type de projet invalide";
    Class<?> [] groups() default {};
    Class<? extends Payload> [] payload() default {};

    @ApplicationScoped
    class ValidProjectTypeValidator implements ConstraintValidator<ValidProjectType, String>
    {
        @Inject
        TypeRepo typeRepo;

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context)
        {
            if(value == null) return true;
            return typeRepo.existsByCodeAndGroupCode(value, "PROJECT_TYPE");
        }
    }
}