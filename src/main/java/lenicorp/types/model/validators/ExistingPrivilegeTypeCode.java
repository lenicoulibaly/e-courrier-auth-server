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

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ExistingPrivilegeTypeCode.Validator.class)
@Documented
public @interface ExistingPrivilegeTypeCode
{
    String message() default "Type de privilège inconnu";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    boolean allowNull() default true;

    @ApplicationScoped
    class Validator implements ConstraintValidator<ExistingPrivilegeTypeCode, String>
    {
        @Inject
        private TypeRepo typeRepo;
        private boolean allowNull;

        @Override
        public void initialize(ExistingPrivilegeTypeCode constraintAnnotation)
        {
            this.allowNull = constraintAnnotation.allowNull();
        }

        @Override
        public boolean isValid(String code, ConstraintValidatorContext context) 
        {
            if (StringUtils.isBlank(code)) return allowNull;
            return typeRepo.existsByCodeAndGroupCode(code.toUpperCase(), "PRV");
        }
    }
}