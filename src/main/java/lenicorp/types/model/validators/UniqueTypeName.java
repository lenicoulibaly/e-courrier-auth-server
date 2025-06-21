package lenicorp.types.model.validators;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lenicorp.types.controller.repositories.TypeRepo;
import lenicorp.utilities.StringUtils;
import lenicorp.types.model.dtos.TypeDTO;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {UniqueTypeName.FieldValidator.class, UniqueTypeName.TypeValidator.class})
@Documented
public @interface UniqueTypeName 
{
    String message() default "Le nom de type '{validatedValue}' existe déjà";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @ApplicationScoped
    class FieldValidator implements ConstraintValidator<UniqueTypeName, String> 
    {
        @Inject
        private TypeRepo typeRepo;

        @Override
        public boolean isValid(String name, ConstraintValidatorContext context) 
        {
            if (StringUtils.isBlank(name)) return true;
            return !typeRepo.existsByName(name);
        }
    }

    @ApplicationScoped
    class TypeValidator implements ConstraintValidator<UniqueTypeName, TypeDTO> 
    {
        @Inject
        private TypeRepo typeRepo;

        @Override
        public boolean isValid(TypeDTO dto, ConstraintValidatorContext context)
        {
            if (dto == null || StringUtils.isBlank(dto.getName())) return true;
            String name = dto.getName();
            String code = dto.getCode();
            boolean exists = StringUtils.isBlank(code) ? 
                typeRepo.existsByName(name) : 
                typeRepo.existsByName(name, code);
            return !exists;
        }
    }
}