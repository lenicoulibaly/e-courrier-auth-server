package lenicorp.security.model.validators;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lenicorp.security.controller.repositories.spec.IUserRepo;
import lombok.RequiredArgsConstructor;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ActiveUser.ActiveUserValidatorByUserId.class, ActiveUser.ActiveUserValidatorByUsername.class})
@Documented
public @interface ActiveUser
{
    String message() default "Votre compte utilisateur n'est pas actif";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    boolean allowNull() default true;

    @ApplicationScoped @RequiredArgsConstructor
    class ActiveUserValidatorByUserId implements ConstraintValidator<ActiveUser, Long>
    {
        private final IUserRepo userRepo;
        private boolean allowNull;

        @Override
        public void initialize(ActiveUser constraintAnnotation)
        {
            this.allowNull = constraintAnnotation.allowNull();
        }

        @Override
        public boolean isValid(Long userId, ConstraintValidatorContext context)
        {
            if (userId == null) return allowNull;
            return userRepo.userIsActive(userId);
        }
    }

    @ApplicationScoped @RequiredArgsConstructor
    class ActiveUserValidatorByUsername implements ConstraintValidator<ActiveUser, String>
    {
        private final IUserRepo userRepo;
        private boolean allowNull;

        @Override
        public void initialize(ActiveUser constraintAnnotation)
        {
            this.allowNull = constraintAnnotation.allowNull();
        }

        @Override
        public boolean isValid(String username, ConstraintValidatorContext context)
        {
            if (username == null) return allowNull;
            if (!userRepo.existsByEmail(username)) return true;
            return userRepo.userIsActive(username);
        }
    }
}