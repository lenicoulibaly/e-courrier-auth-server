package lenicorp.association.controller.services;

import jakarta.transaction.Transactional;
import lenicorp.association.model.dtos.AdhesionDTO;
import lenicorp.association.model.entities.Adhesion;
import lenicorp.utilities.Page;
import lenicorp.utilities.PageRequest;
import lenicorp.utilities.SelectOption;

import java.util.List;
import java.util.Optional;

public interface IAdhesionService
{
    Adhesion createUserAndAdhesion(AdhesionDTO dto);

    @Transactional
    Adhesion updateMembre(AdhesionDTO dto);

    void seDesabonner(Long adhesionId);
    Page<AdhesionDTO> searchAdhsions(String key, Long assoId, Long sectionId, PageRequest pageable);
    AdhesionDTO getMembreDTO(String uniqueIdentifier);
    List<SelectOption> getOptions(Long assoId);

    Optional<Adhesion> findByEmailAndSection(String email, Long sectionId);

    Optional<Adhesion> findByEmailAndAsso(String email, Long assoId);
}
