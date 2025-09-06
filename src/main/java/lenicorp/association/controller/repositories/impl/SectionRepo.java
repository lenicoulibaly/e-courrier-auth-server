package lenicorp.association.controller.repositories.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import lenicorp.association.controller.repositories.spec.ISectionRepo;
import lenicorp.association.model.dtos.ReadSectionDTO;
import lenicorp.association.model.entities.Section;
import lenicorp.utilities.Page;
import lenicorp.utilities.PageRequest;

import java.util.List;

@ApplicationScoped
public class SectionRepo implements ISectionRepo
{
    @Inject
    EntityManager em;

    @Override
    public boolean existsById(Long id) {
        return count("sectionId", id) > 0;
    }

    @Override
    public Page<ReadSectionDTO> searchSections(String key, Long assoId, Long strId, PageRequest pageRequest)
    {
        String baseQuery = """
            FROM Section s 
            LEFT JOIN s.association a 
            LEFT JOIN s.strTutelle str 
            WHERE 
            (
                UPPER(FUNCTION('unaccent', COALESCE(s.sectionName, ''))) LIKE :key
                OR UPPER(FUNCTION('unaccent', COALESCE(s.sigle, ''))) LIKE :key
                OR UPPER(FUNCTION('unaccent', COALESCE(s.situationGeo, ''))) LIKE :key
                OR UPPER(FUNCTION('unaccent', COALESCE(a.assoName, ''))) LIKE :key
                OR UPPER(FUNCTION('unaccent', COALESCE(a.sigle, ''))) LIKE :key
                OR UPPER(FUNCTION('unaccent', COALESCE(str.strName, ''))) LIKE :key
                OR UPPER(FUNCTION('unaccent', COALESCE(str.strSigle, ''))) LIKE :key
            )
            AND (:assoId IS NULL OR a.assoId = :assoId)
            AND (:strId IS NULL OR str.strId = :strId)
            """;

        String selectQuery = "SELECT NEW lenicorp.association.model.dtos.ReadSectionDTO(" +
                "s.sectionId, s.sectionName, s.situationGeo, s.sigle, a.assoId, a.assoName) " + baseQuery;
        String countQuery = "SELECT COUNT(s) " + baseQuery;

        key = "%" + key.toUpperCase() + "%";

        Long count = em.createQuery(countQuery, Long.class)
                .setParameter("key", key)
                .setParameter("assoId", assoId)
                .setParameter("strId", strId)
                .getSingleResult();

        var content = em.createQuery(selectQuery, ReadSectionDTO.class)
                .setParameter("key", key)
                .setParameter("assoId", assoId)
                .setParameter("strId", strId)
                .setFirstResult(pageRequest.getPage() * pageRequest.getSize())
                .setMaxResults(pageRequest.getSize())
                .getResultList();

        return new Page<>(content, count, pageRequest.getPage(), pageRequest.getSize());
    }

    @Override
    public boolean existsByNameAndAssoId(String sectionName, Long assoId)
    {
        return count("SELECT COUNT(s) FROM Section s WHERE TRIM(UPPER(s.sectionName)) = TRIM(UPPER(?1)) AND s.association.assoId = ?2", sectionName, assoId) > 0;
    }

    @Override
    public List<ReadSectionDTO> findbyAssoId(Long assoId)
    {
        return em.createQuery("""
            SELECT NEW lenicorp.association.model.dtos.ReadSectionDTO(
                s.sectionId, s.sectionName, s.situationGeo, s.sigle, s.association.assoId, s.association.assoName)
            FROM Section s WHERE s.association.assoId = :assoId
            """, ReadSectionDTO.class)
                .setParameter("assoId", assoId)
                .getResultList();
    }
}
