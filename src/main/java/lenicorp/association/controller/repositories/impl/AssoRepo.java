package lenicorp.association.controller.repositories.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import lenicorp.association.controller.repositories.spec.IAssoRepo;
import lenicorp.association.model.dtos.ReadAssociationDTO;
import lenicorp.utilities.Page;
import lenicorp.utilities.PageRequest;

@ApplicationScoped
public class AssoRepo implements IAssoRepo
{
    @Inject
    EntityManager em;

    @Override
    public boolean existsById(Long id) {
        return count("assoId", id) > 0;
    }

    @Override
    public Page<ReadAssociationDTO> searchAssociations(String key, PageRequest pageRequest)
    {
        String baseQuery = """
            FROM Association a 
            WHERE 
            (
                UPPER(FUNCTION('unaccent', COALESCE(a.assoName, ''))) LIKE :key
                OR UPPER(FUNCTION('unaccent', COALESCE(a.situationGeo, ''))) LIKE :key
                OR UPPER(FUNCTION('unaccent', COALESCE(a.sigle, ''))) LIKE :key
            )
            """;

        String selectQuery = "SELECT NEW lenicorp.association.model.dtos.ReadAssociationDTO(" +
                "a.assoId, a.assoName, a.situationGeo, a.sigle, a.droitAdhesion) " + baseQuery;
        String countQuery = "SELECT COUNT(a) " + baseQuery;

        key = "%" + key.toUpperCase() + "%";

        Long count = em.createQuery(countQuery, Long.class)
                .setParameter("key", key)
                .getSingleResult();

        var content = em.createQuery(selectQuery, ReadAssociationDTO.class)
                .setParameter("key", key)
                .setFirstResult(pageRequest.getPage() * pageRequest.getSize())
                .setMaxResults(pageRequest.getSize())
                .getResultList();

        return new Page<>(content, count, pageRequest.getPage(), pageRequest.getSize());
    }

    @Override
    public String getStrCode(Long strId)
    {
        return em.createQuery("SELECT s.strSigle FROM Structure s WHERE s.strId = :strId", String.class)
                .setParameter("strId", strId)
                .getSingleResult();
    }

    @Override
    public boolean existsByName(String assoName)
    {
        return count("SELECT COUNT(a) FROM Association a WHERE TRIM(UPPER(a.assoName)) = TRIM(UPPER(?1))", assoName) > 0;
    }

    @Override
    public boolean existsByName(String assoName, Long assoId)
    {
        return count("SELECT COUNT(a) FROM Association a WHERE TRIM(UPPER(a.assoName)) = TRIM(UPPER(?1)) AND a.assoId <> ?2", assoName, assoId) > 0;
    }

    @Override
    public ReadAssociationDTO findReadAssoDtoById(Long assoId)
    {
        return em.createQuery("""
            SELECT NEW lenicorp.association.model.dtos.ReadAssociationDTO(
                a.assoId, a.assoName, a.situationGeo, a.sigle, a.droitAdhesion)
            FROM Association a WHERE a.assoId = :assoId
            """, ReadAssociationDTO.class)
                .setParameter("assoId", assoId)
                .getSingleResult();
    }

    @Override
    public String getSigleByAssoId(Long assoId)
    {
        return em.createQuery("SELECT a.sigle FROM Association a WHERE a.assoId = :assoId", String.class)
                .setParameter("assoId", assoId)
                .getSingleResult();
    }
}
