package lenicorp.types.model.validators;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lenicorp.types.controller.repositories.TypeGroupRepo;
import lenicorp.utilities.StringUtils;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueGroupCode.Validator.class)
@Documented
public @interface UniqueGroupCode 
{
    String message() default "Le code de groupe '{validatedValue}' existe déjà";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    boolean allowNull() default false;

    @ApplicationScoped
    class Validator implements ConstraintValidator<UniqueGroupCode, String> 
    {
        @Inject
        private TypeGroupRepo typeGroupRepo;
        
        private boolean allowNull = true;

        @Override
        public void initialize(UniqueGroupCode constraintAnnotation) 
        {
            this.allowNull = constraintAnnotation.allowNull();
        }

        @Override
        public boolean isValid(String groupCode, ConstraintValidatorContext context) 
        {
            if (StringUtils.isBlank(groupCode)) return allowNull;
            return !typeGroupRepo.existsByGroupCode(groupCode.toUpperCase());
        }
    }
}