package lenicorp.paiement.controller.repositories.spec;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import lenicorp.paiement.model.entities.Echeancier;

/**
 * Repository interface for Echeancier entities
 */
public interface IEcheancierRepo extends PanacheRepository<Echeancier>
{
    /**
     * Check if an Echeancier exists by its ID
     * @param id the ID to check
     * @return true if an Echeancier with the given ID exists, false otherwise
     */
    boolean existsById(Long id);
}