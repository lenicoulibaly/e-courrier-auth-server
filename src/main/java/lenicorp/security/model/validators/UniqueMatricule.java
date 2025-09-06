package lenicorp.security.model.validators;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lenicorp.association.model.dtos.AdhesionDTO;
import lenicorp.security.controller.repositories.impl.UserRepo;
import lenicorp.security.model.dtos.UserProfileAssoDTO;
import lombok.RequiredArgsConstructor;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {UniqueMatricule.UniqueMatriculeValidatorOnCreate.class, UniqueMatricule.UniqueMatriculeValidatorOnUpdate.class})
@Documented
public @interface UniqueMatricule
{
    String message() default "Matricule déjà utilisé";
    Class<?> [] groups() default {};
    Class<? extends Payload> [] payload() default {};


    @ApplicationScoped @RequiredArgsConstructor
    class UniqueMatriculeValidatorOnUpdate implements ConstraintValidator<UniqueMatricule, AdhesionDTO>
    {
        private final UserRepo userRepo;

        @Override
        public boolean isValid(AdhesionDTO dto, ConstraintValidatorContext context)
        {
            if(dto == null) return true;
            if(dto.getMatricule() == null || dto.getUserId() == null) return true;
            return !userRepo.existsByMatricule(dto.getMatricule(), dto.getUserId()) ;
        }
    }

    @ApplicationScoped @RequiredArgsConstructor
    class UniqueMatriculeValidatorOnUpdateUserAndProfile implements ConstraintValidator<UniqueMatricule, UserProfileAssoDTO>
    {
        private final UserRepo userRepo;

        @Override
        public boolean isValid(UserProfileAssoDTO dto, ConstraintValidatorContext context)
        {
            return !userRepo.existsByMatricule(dto.getMatricule(), dto.getUserId()) ;
        }
    }

    @ApplicationScoped @RequiredArgsConstructor
    class UniqueMatriculeValidatorOnCreate implements ConstraintValidator<UniqueMatricule, String>
    {
        private final UserRepo userRepo;

        @Override
        public boolean isValid(String matricule, ConstraintValidatorContext context)
        {
            return !userRepo.existsByMatricule(matricule) ;
        }
    }
}