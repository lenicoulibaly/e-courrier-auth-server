package lenicorp.structures.model.validators;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lenicorp.structures.controller.repositories.StrRepo;
import lenicorp.structures.model.dtos.CreateOrUpdateStrDTO;
import lenicorp.utilities.StringUtils;
import lombok.RequiredArgsConstructor;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {
    UniqueSigleUnderSameParent.UniqueSigleUnderSameParentValidator.class
})
@Documented
public @interface UniqueSigleUnderSameParent 
{
    String message() default "Ce sigle existe déjà sous le même parent";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @ApplicationScoped
    @RequiredArgsConstructor
    class UniqueSigleUnderSameParentValidator implements ConstraintValidator<UniqueSigleUnderSameParent, CreateOrUpdateStrDTO>
    {
        private final StrRepo strRepo;

        @Override
        public boolean isValid(CreateOrUpdateStrDTO dto, ConstraintValidatorContext context)
        {
            if (dto == null || StringUtils.isBlank(dto.getStrSigle()))
            {
                return true;
            }

            Long excludeStrId = (dto.getStrId() != null) ? dto.getStrId() : null;

            return !strRepo.sigleExistsUnderSameParent(
                    dto.getStrSigle(),
                    dto.getParentId(),
                    excludeStrId
            );
        }
    }
}