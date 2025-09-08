package lenicorp.paiement.model.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {ExistingEcheanceIdValidator.class})
public @interface ExistingEcheanceId
{
    String message() default "Echeance introuvable";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}