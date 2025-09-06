package lenicorp.association.model.validators;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lenicorp.association.controller.repositories.impl.AssoRepo;
import lenicorp.association.controller.repositories.spec.IAssoRepo;
import lenicorp.association.model.dtos.UpdateAssociationDTO;
import lombok.RequiredArgsConstructor;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {UniqueAssoName.UniqueAssoNameValidatorOnCreate.class, UniqueAssoName.UniqueAssoNameValidatorOnUpdate.class})
@Documented
public @interface UniqueAssoName
{
    String message() default "Nom d'association déjà utilisé";
    Class<?> [] groups() default {};
    Class<? extends Payload> [] payload() default {};

    @ApplicationScoped @RequiredArgsConstructor
    class UniqueAssoNameValidatorOnUpdate implements ConstraintValidator<UniqueAssoName, UpdateAssociationDTO>
    {
        private final IAssoRepo assoRepo;

        @Override
        public boolean isValid(UpdateAssociationDTO dto, ConstraintValidatorContext context)
        {
            if(dto == null) return true;
            if(dto.getAssoName() == null || dto.getAssoId() == null) return true;
            return !assoRepo.existsByName(dto.getAssoName(), dto.getAssoId()) ;
        }
    }

    @ApplicationScoped @RequiredArgsConstructor
    class UniqueAssoNameValidatorOnCreate implements ConstraintValidator<UniqueAssoName, String>
    {
        private final AssoRepo assoRepo;

        @Override
        public boolean isValid(String name, ConstraintValidatorContext context)
        {
            return !assoRepo.existsByName(name) ;
        }
    }
}