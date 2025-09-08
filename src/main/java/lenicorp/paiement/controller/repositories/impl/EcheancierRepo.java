package lenicorp.paiement.controller.repositories.impl;

import jakarta.enterprise.context.ApplicationScoped;
import lenicorp.paiement.controller.repositories.spec.IEcheancierRepo;
import lenicorp.paiement.model.entities.Echeancier;

/**
 * Repository implementation for Echeancier entities
 */
@ApplicationScoped
public class EcheancierRepo implements IEcheancierRepo
{
    /**
     * Check if an Echeancier exists by its ID
     * @param id the ID to check
     * @return true if an Echeancier with the given ID exists, false otherwise
     */
    @Override
    public boolean existsById(Long id) {
        return findById(id) != null;
    }
}