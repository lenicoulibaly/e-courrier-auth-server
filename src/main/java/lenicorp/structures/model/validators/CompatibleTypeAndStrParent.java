package lenicorp.structures.model.validators;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lenicorp.structures.controller.repositories.StrRepo;
import lenicorp.structures.model.dtos.ChangeAnchorDTO;
import lenicorp.structures.model.dtos.CreateOrUpdateStrDTO;
import lombok.RequiredArgsConstructor;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {CompatibleTypeAndStrParent.CompatibleTypeAndParentStrValidatorOnCreate.class, CompatibleTypeAndStrParent.CompatibleTypeAndParentStrValidator.class})
@Documented
public @interface CompatibleTypeAndStrParent
{
    String message() default "Imcompatibilité de type : Impossible de loger une structure dans une tutelle de type inférieur";
    Class<?> [] groups() default {};
    Class<? extends Payload> [] payload() default {};
    @ApplicationScoped
    @RequiredArgsConstructor
    class CompatibleTypeAndParentStrValidator implements ConstraintValidator<CompatibleTypeAndStrParent, ChangeAnchorDTO>
    {
        private final StrRepo strRepo;
        @Override
        public boolean isValid(ChangeAnchorDTO dto, ConstraintValidatorContext context)
        {
            if(dto.getNewParentId()==null) return true;
            return strRepo.parentHasCompatibleSousType(dto.getNewParentId(), dto.getNewTypeCode());
        }
    }

    @ApplicationScoped @RequiredArgsConstructor
    class CompatibleTypeAndParentStrValidatorOnCreate implements ConstraintValidator <CompatibleTypeAndStrParent, CreateOrUpdateStrDTO>
    {
        private final StrRepo strRepo;
        @Override
        public boolean isValid(CreateOrUpdateStrDTO dto, ConstraintValidatorContext context)
        {
            if(dto.getParentId()==null) return true;
            return strRepo.parentHasCompatibleSousType(dto.getParentId(), dto.getTypeCode());
        }
    }
}
