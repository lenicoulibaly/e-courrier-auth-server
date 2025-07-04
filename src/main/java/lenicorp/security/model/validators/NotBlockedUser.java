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
@Constraint(validatedBy = {NotBlockedUser.NotBlockedUserValidatorByUserId.class, NotBlockedUser.NotBlockedUserValidatorByUsername.class})
@Documented
public @interface NotBlockedUser
{
    String message() default "Votre compte utilisateur est bloqué";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    boolean allowNull() default true;

    @ApplicationScoped @RequiredArgsConstructor
    class NotBlockedUserValidatorByUserId implements ConstraintValidator<NotBlockedUser, Long>
    {
        private final IUserRepo userRepo;
        private boolean allowNull;

        @Override
        public void initialize(NotBlockedUser constraintAnnotation)
        {
            this.allowNull = constraintAnnotation.allowNull();
        }

        @Override
        public boolean isValid(Long userId, ConstraintValidatorContext context)
        {
            if (userId == null) return allowNull;
            return userRepo.userIsNotBlocked(userId);
        }
    }

    @ApplicationScoped @RequiredArgsConstructor
    class NotBlockedUserValidatorByUsername implements ConstraintValidator<NotBlockedUser, String>
    {
        private final IUserRepo userRepo;
        private boolean allowNull;

        @Override
        public void initialize(NotBlockedUser constraintAnnotation)
        {
            this.allowNull = constraintAnnotation.allowNull();
        }

        @Override
        public boolean isValid(String username, ConstraintValidatorContext context)
        {
            if (username == null) return allowNull;
            if (!userRepo.existsByEmail(username)) return true;
            return userRepo.userIsNotBlocked(username);
        }
    }
}