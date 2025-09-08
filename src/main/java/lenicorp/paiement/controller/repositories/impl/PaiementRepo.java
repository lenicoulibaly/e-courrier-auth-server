package lenicorp.paiement.controller.repositories.impl;

import jakarta.enterprise.context.ApplicationScoped;
import lenicorp.paiement.controller.repositories.spec.IPaiementRepo;
import lenicorp.paiement.model.entities.Paiement;
import lenicorp.utilities.StringUtils;

/**
 * Repository implementation for Paiement entities
 */
@ApplicationScoped
public class PaiementRepo implements IPaiementRepo
{
    /**
     * Check if a Paiement exists by its reference
     * @param reference the reference to check
     * @return true if a Paiement with the given reference exists, false otherwise
     */
    @Override
    public boolean existsByReference(String reference) {
        if(StringUtils.isBlank(reference)) return false;
        return count("upper(reference) = ?1", reference.toUpperCase()) > 0;
    }

    /**
     * Check if a Paiement exists by its reference, excluding a specific Paiement
     * @param reference the reference to check
     * @param paiementId the ID of the Paiement to exclude
     * @return true if a Paiement with the given reference exists (excluding the specified Paiement), false otherwise
     */
    @Override
    public boolean existsByReference(String reference, Long paiementId) {
        if(StringUtils.isBlank(reference)) return false;
        return count("upper(reference) = ?1 and paiementId <> ?2", reference.toUpperCase(), paiementId) > 0;
    }
}