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
@Constraint(validatedBy = ExistingTypeCode.Validator.class)
@Documented
public @interface ExistingTypeCode 
{
    String message() default "Le type avec le code '{validatedValue}' n'existe pas";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    boolean allowNull() default false;
    String typeGroupCode() default "";

    @ApplicationScoped
    class Validator implements ConstraintValidator<ExistingTypeCode, String> 
    {
        private boolean allowNull;
        private String typeGroupCode;

        @Override
        public void initialize(ExistingTypeCode constraintAnnotation) 
        {
            this.allowNull = constraintAnnotation.allowNull();
            this.typeGroupCode = constraintAnnotation.typeGroupCode();
        }

        @Override
        public boolean isValid(String code, ConstraintValidatorContext context) 
        {
            ITypeRepo typeRepo = CDI.current().select(ITypeRepo.class).get();
            if (StringUtils.isBlank(code)) return allowNull;
            if(typeGroupCode.equals("")) return typeRepo.existsByCode(code.toUpperCase());
            return typeRepo.existsByCodeAndGroupCode(code.toUpperCase(), typeGroupCode);
        }
    }
}