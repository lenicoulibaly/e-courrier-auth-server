package lenicorp.association.controller.repositories.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import lenicorp.association.controller.repositories.spec.IAdhesionRepo;
import lenicorp.association.model.dtos.AdhesionDTO;
import lenicorp.association.model.entities.Adhesion;
import lenicorp.utilities.Page;
import lenicorp.utilities.PageRequest;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class AdhesionRepo implements IAdhesionRepo
{
    @Inject
    EntityManager em;

    @Override
    public boolean existsById(Long id) {
        return count("adhesionId", id) > 0;
    }

    @Override
    public Page<AdhesionDTO> searchAdhesions(String key, List<String> usersIds, Long assoId, Long sectionId, PageRequest pageRequest)
    {
        String baseQuery = """
            FROM Adhesion a 
            LEFT JOIN a.section sect 
            LEFT JOIN sect.association sectAsso 
            LEFT JOIN a.association asso 
            WHERE 
            (
                UPPER(FUNCTION('unaccent', COALESCE(sect.sectionName, ''))) LIKE :key 
                OR UPPER(FUNCTION('unaccent', COALESCE(asso.assoName, ''))) LIKE :key
            )
            AND (:usersIds IS NULL OR a.userId IN :usersIds)
            AND ((asso.assoId = :assoId OR sectAsso.assoId = :assoId) OR (sect.sectionId = :sectionId))
            """;

        String selectQuery = "SELECT NEW lenicorp.association.model.dtos.AdhesionDTO(" +
                "CAST(a.userId AS long), sect.sectionId, asso.assoId, a.adhesionId, sect.sectionName, asso.assoName) " + baseQuery;
        String countQuery = "SELECT COUNT(a) " + baseQuery;

        key = "%" + key.toUpperCase() + "%";

        Long count = em.createQuery(countQuery, Long.class)
                .setParameter("key", key)
                .setParameter("usersIds", usersIds)
                .setParameter("assoId", assoId)
                .setParameter("sectionId", sectionId)
                .getSingleResult();

        List<AdhesionDTO> content = em.createQuery(selectQuery, AdhesionDTO.class)
                .setParameter("key", key)
                .setParameter("usersIds", usersIds)
                .setParameter("assoId", assoId)
                .setParameter("sectionId", sectionId)
                .setFirstResult(pageRequest.getPage() * pageRequest.getSize())
                .setMaxResults(pageRequest.getSize())
                .getResultList();

        return new Page<>(content, count, pageRequest.getPage(), pageRequest.getSize());
    }

    @Override
    public Optional<Adhesion> findByUserIdAndSectionId(String keycloakUserId, Long sectionId)
    {
        return find("userId = ?1 AND section.sectionId = ?2", keycloakUserId, sectionId).firstResultOptional();
    }

    @Override
    public Optional<Adhesion> findByUserIdAndAsso(String keycloakUserId, Long assoId)
    {
        return find("userId = ?1 AND association.assoId = ?2", keycloakUserId, assoId).firstResultOptional();
    }

    @Override
    public List<Adhesion> getAdhesionsByAssoId(Long assoId)
    {
        return list("association.assoId = ?1", assoId);
    }
}
