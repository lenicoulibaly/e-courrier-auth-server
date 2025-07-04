package lenicorp.security.model.validators;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lenicorp.security.controller.repositories.spec.IAuthorityRepo;
import lenicorp.security.model.dtos.AuthorityDTO;
import lombok.RequiredArgsConstructor;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {UniqueAuthName.UniqueAuthNameValidatorForCreate.class, UniqueAuthName.UniqueAuthNameValidatorForUpdate.class})
@Documented
@Repeatable(UniqueAuthName.List.class)
public @interface UniqueAuthName
{
    String message() default "Nom déjà utilisé";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    boolean allowNull() default true;

    @Target({ElementType.FIELD}) @Retention(RetentionPolicy.RUNTIME) @Documented
    @interface List
    {
        UniqueAuthName[] value();
    }

    @ApplicationScoped
    @RequiredArgsConstructor
    class UniqueAuthNameValidatorForCreate implements ConstraintValidator<UniqueAuthName, String>
    {
        private final IAuthorityRepo authorityRepo;
        private boolean allowNull;

        @Override
        public void initialize(UniqueAuthName constraintAnnotation)
        {
            this.allowNull = constraintAnnotation.allowNull();
        }

        @Override
        public boolean isValid(String authName, ConstraintValidatorContext context)
        {
            if (authName == null) return allowNull;
            return !authorityRepo.existsByName(authName);
        }
    }

    @ApplicationScoped
    @RequiredArgsConstructor
    class UniqueAuthNameValidatorForUpdate implements ConstraintValidator<UniqueAuthName, AuthorityDTO>
    {
        private final IAuthorityRepo authorityRepo;
        private boolean allowNull;

        @Override
        public void initialize(UniqueAuthName constraintAnnotation)
        {
            this.allowNull = constraintAnnotation.allowNull();
        }

        @Override
        public boolean isValid(AuthorityDTO dto, ConstraintValidatorContext context)
        {
            if (dto == null) return true;
            String authName = dto.getName();
            String authCode = dto.getCode();
            if (authName == null || authCode == null) return true;
            return !authorityRepo.existsByName(authName, authCode);
        }
    }
}