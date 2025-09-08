package lenicorp.paiement.controller.repositories.spec;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import lenicorp.paiement.model.entities.Paiement;

/**
 * Repository interface for Paiement entities
 */
public interface IPaiementRepo extends PanacheRepository<Paiement>
{
    /**
     * Check if a Paiement exists by its reference
     * @param reference the reference to check
     * @return true if a Paiement with the given reference exists, false otherwise
     */
    boolean existsByReference(String reference);

    /**
     * Check if a Paiement exists by its reference, excluding a specific Paiement
     * @param reference the reference to check
     * @param paiementId the ID of the Paiement to exclude
     * @return true if a Paiement with the given reference exists (excluding the specified Paiement), false otherwise
     */
    boolean existsByReference(String reference, Long paiementId);
}