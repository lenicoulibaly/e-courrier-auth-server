package lenicorp.paiement.controller.repositories.impl;

import jakarta.enterprise.context.ApplicationScoped;
import lenicorp.paiement.controller.repositories.spec.IEcheanceRepo;
import lenicorp.paiement.model.entities.Echeance;

/**
 * Repository implementation for Echeance entities
 */
@ApplicationScoped
public class EcheanceRepo implements IEcheanceRepo
{
    /**
     * Check if an Echeance exists by its ID
     * @param id the ID to check
     * @return true if an Echeance with the given ID exists, false otherwise
     */
    @Override
    public boolean existsById(Long id) {
        return findById(id) != null;
    }
}
