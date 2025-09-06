package lenicorp.archive.model.dtos.validator;

import jakarta.inject.Inject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lenicorp.types.controller.repositories.TypeRepo;
import lombok.RequiredArgsConstructor;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ValidDocType.ValidArchiveTypeValidator.class})
@Documented
public @interface ValidDocType
{
    String message() default "Type d'archive invalide";
    Class<?> [] groups() default {};
    Class<? extends Payload> [] payload() default {};

    @ApplicationScoped
    class ValidArchiveTypeValidator implements ConstraintValidator<ValidDocType, String>
    {
        @Inject
        TypeRepo typeRepo;

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context)
        {
            if(value == null) return true;
            return typeRepo.existsByCodeAndGroupCode(value, "DOCUMENT");
        }
    }
}
