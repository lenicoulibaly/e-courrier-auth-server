package lenicorp.paiement.model.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lenicorp.paiement.controller.repositories.spec.IEcheanceRepo;

@ApplicationScoped
@RequiredArgsConstructor
public class ExistingEcheanceIdValidator implements ConstraintValidator<ExistingEcheanceId, Long>
{
    private final IEcheanceRepo echeanceRepo;

    @Override
    public boolean isValid(Long echeanceId, ConstraintValidatorContext context)
    {
        if(echeanceId == null) return true;
        return echeanceRepo.existsById(echeanceId);
    }
}
/*
Caused by: java.lang.NoSuchMethodException: rigeldevsolutions.gestasso.metier.cotisationmodule.model.validators.ExistingCotisationIdValidator.<init>()
	at java.base/java.lang.Class.getConstructor0(Class.java:3585)
	at java.base/java.lang.Class.getConstructor(Class.java:2271)
	at org.hibernate.validator.internal.util.privilegedactions.NewInstance.run(NewInstance.java:41)
	... 163 more
 */
