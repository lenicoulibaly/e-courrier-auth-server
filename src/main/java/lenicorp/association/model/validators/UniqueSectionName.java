package lenicorp.association.model.validators;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lenicorp.association.controller.repositories.spec.ISectionRepo;
import lenicorp.association.model.dtos.CreateSectionDTO;
import lombok.RequiredArgsConstructor;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {UniqueSectionName.UniqueSectionNameValidator.class})
@Documented
public @interface UniqueSectionName
{
    String message() default "Nom de section déjà utilisé";
    Class<?> [] groups() default {};
    Class<? extends Payload> [] payload() default {};

    @ApplicationScoped @RequiredArgsConstructor
    class UniqueSectionNameValidator implements ConstraintValidator<UniqueSectionName, CreateSectionDTO>
    {
        private final ISectionRepo sectionRepo;

        @Override
        public boolean isValid(CreateSectionDTO dto, ConstraintValidatorContext context)
        {
            if(dto == null) return true;
            return !sectionRepo.existsByNameAndAssoId(dto.getSectionName(), dto.getAssoId()) ;
        }
    }
}