package lenicorp.security.model.validators;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lenicorp.security.controller.repositories.spec.IAuthAssoRepo;
import lenicorp.security.model.entities.AuthAssociation;
import lombok.RequiredArgsConstructor;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ExistingAuthAssoId.Validator.class)
@Documented
public @interface ExistingAuthAssoId
{
    String message() default "L'association avec ID '{validatedValue}' n'existe pas ou n'est pas de type USR_PRFL";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    boolean allowNull() default false;

    @ApplicationScoped @RequiredArgsConstructor
    class Validator implements ConstraintValidator<ExistingAuthAssoId, Long>
    {
        private final IAuthAssoRepo authAssoRepo;
        private boolean allowNull;

        @Override
        public void initialize(ExistingAuthAssoId constraintAnnotation)
        {
            this.allowNull = constraintAnnotation.allowNull();
        }

        @Override
        public boolean isValid(Long id, ConstraintValidatorContext context)
        {
            if (id == null) return allowNull;

            AuthAssociation association = authAssoRepo.findById(id);
            if (association == null) return false;

            return association.getType() != null && "USR_PRFL".equals(association.getType().code);
        }
    }
}
