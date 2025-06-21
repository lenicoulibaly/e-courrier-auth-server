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
@Constraint(validatedBy = ExistingGroupCode.Validator.class)
@Documented
public @interface ExistingGroupCode
{
    String message() default "Le groupe de type avec le code '{validatedValue}' n'existe pas";
    
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};

    boolean allowNull() default true;

    @ApplicationScoped
    class Validator implements ConstraintValidator<ExistingGroupCode, String>
    {
        @Inject
        private TypeGroupRepo typeGroupRepo;
        private boolean allowNull;

        @Override
        public void initialize(ExistingGroupCode constraintAnnotation)
        {
            this.allowNull = constraintAnnotation.allowNull();
        }

        @Override
        public boolean isValid(String groupCode, ConstraintValidatorContext context) 
        {
            if (StringUtils.isBlank(groupCode)) return allowNull;
            return typeGroupRepo.existsByGroupCode(groupCode.toUpperCase());
        }
    }
}