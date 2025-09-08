package lenicorp.paiement.controller.repositories.spec;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import lenicorp.paiement.model.entities.Echeance;

/**
 * Repository interface for Echeance entities
 */
public interface IEcheanceRepo extends PanacheRepository<Echeance>
{
    /**
     * Check if an Echeance exists by its ID
     * @param id the ID to check
     * @return true if an Echeance with the given ID exists, false otherwise
     */
    boolean existsById(Long id);
}
