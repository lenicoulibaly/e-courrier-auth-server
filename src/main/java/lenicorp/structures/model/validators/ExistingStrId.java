package lenicorp.structures.model.validators;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lenicorp.structures.controller.repositories.StrRepo;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ExistingStrId.Validator.class)
@Documented
public @interface ExistingStrId
{
    String message() default "Structure introuvable avec l'identifiant '{validatedValue}'";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    boolean allowNull() default false;

    @ApplicationScoped
    class Validator implements ConstraintValidator<ExistingStrId, Long>
    {
        @Inject
        private StrRepo strRepo;
        private boolean allowNull = true;

        @Override
        public void initialize(ExistingStrId constraintAnnotation)
        {
            this.allowNull = constraintAnnotation.allowNull();
        }

        @Override
        public boolean isValid(Long strId, ConstraintValidatorContext context)
        {
            if (strId == null) return allowNull;
            return strRepo.existsById(strId);
        }
    }
}