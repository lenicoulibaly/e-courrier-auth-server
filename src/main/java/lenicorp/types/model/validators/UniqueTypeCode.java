package lenicorp.types.model.validators;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lenicorp.types.controller.repositories.TypeRepo;
import lenicorp.utilities.StringUtils;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueTypeCode.Validator.class)
@Documented
public @interface UniqueTypeCode 
{
    String message() default "Le code de type '{validatedValue}' existe déjà";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    boolean allowNull() default false;

    @ApplicationScoped
    class Validator implements ConstraintValidator<UniqueTypeCode, String> 
    {
        @Inject
        private TypeRepo typeRepo;
        private boolean allowNull;

        @Override
        public void initialize(UniqueTypeCode constraintAnnotation) 
        {
            this.allowNull = constraintAnnotation.allowNull();
        }

        @Override
        public boolean isValid(String code, ConstraintValidatorContext context) 
        {
            if (StringUtils.isBlank(code)) return allowNull;
            return !typeRepo.existsByCode(code.toUpperCase());
        }
    }
}