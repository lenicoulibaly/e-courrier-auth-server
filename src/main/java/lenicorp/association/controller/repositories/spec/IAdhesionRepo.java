package lenicorp.association.controller.repositories.spec;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import lenicorp.association.model.dtos.AdhesionDTO;
import lenicorp.association.model.entities.Adhesion;
import lenicorp.utilities.Page;
import lenicorp.utilities.PageRequest;

import java.util.List;
import java.util.Optional;

public interface IAdhesionRepo extends PanacheRepository<Adhesion>
{
    Page<AdhesionDTO> searchAdhesions(String key, List<String> usersIds, Long assoId, Long sectionId, PageRequest pageRequest);
    Optional<Adhesion> findByUserIdAndSectionId(String keycloakUserId, Long sectionId);
    Optional<Adhesion> findByUserIdAndAsso(String keycloakUserId, Long assoId);
    List<Adhesion> getAdhesionsByAssoId(Long assoId);
    boolean existsById(Long id);
}
