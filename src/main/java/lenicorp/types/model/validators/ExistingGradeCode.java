package lenicorp.types.model.validators;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lenicorp.types.controller.repositories.spec.ITypeRepo;
import lenicorp.utilities.StringUtils;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ExistingGradeCode.Validator.class})
@Documented
public @interface ExistingGradeCode
{
    String message() default "Grade inconnu";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    boolean allowNull() default false;

    @ApplicationScoped
    class Validator implements ConstraintValidator<ExistingGradeCode, String>
    {
        private boolean allowNull;

        @Override
        public void initialize(ExistingGradeCode constraintAnnotation)
        {
            this.allowNull = constraintAnnotation.allowNull();
        }

        @Override
        public boolean isValid(String code, ConstraintValidatorContext context)
        {
            ITypeRepo typeRepo = CDI.current().select(ITypeRepo.class).get();
            if (StringUtils.isBlank(code)) return allowNull;
            return typeRepo.existsByCodeAndGroupCode(code.toUpperCase(), "GRADE");
        }
    }
}