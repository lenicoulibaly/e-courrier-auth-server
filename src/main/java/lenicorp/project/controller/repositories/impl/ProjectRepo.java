package lenicorp.project.controller.repositories.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.inject.Inject;
import lenicorp.project.controller.repositories.spec.IProjectRepo;
import lenicorp.project.model.entities.Project;
import lenicorp.utilities.Page;
import lenicorp.utilities.PageRequest;

import java.util.List;

/**
 * Repository implementation for Project entities
 */
@ApplicationScoped
public class ProjectRepo implements IProjectRepo {

    @Inject
    EntityManager em;

    @Override
    public Project findByProjetId(Long projetId) {
        return findById(projetId);
    }

    @Override
    public Page<Project> searchProjects(String key, PageRequest pageable) {
        String jpql = "SELECT p FROM Project p WHERE 1=1";
        String countJpql = "SELECT COUNT(p) FROM Project p WHERE 1=1";

        if (key != null && !key.trim().isEmpty()) {
            String searchCondition = " AND (LOWER(p.nomProjet) LIKE LOWER(:key) " +
                    "OR LOWER(p.description) LIKE LOWER(:key) " +
                    "OR LOWER(p.localisation) LIKE LOWER(:key))";
            jpql += searchCondition;
            countJpql += searchCondition;
        }

        TypedQuery<Project> query = em.createQuery(jpql, Project.class);
        TypedQuery<Long> countQuery = em.createQuery(countJpql, Long.class);

        if (key != null && !key.trim().isEmpty()) {
            query.setParameter("key", "%" + key.trim() + "%");
            countQuery.setParameter("key", "%" + key.trim() + "%");
        }

        query.setFirstResult(pageable.getPage() * pageable.getSize());
        query.setMaxResults(pageable.getSize());

        List<Project> projects = query.getResultList();
        Long total = countQuery.getSingleResult();

        return new Page<>(projects, total.intValue(), pageable.getPage(), pageable.getSize());
    }

    @Override
    public Page<Project> searchProjectsByType(String key, PageRequest pageable, String typeCode) {
        String jpql = "SELECT p FROM Project p JOIN p.typeProjet t WHERE t.code = :typeCode";
        String countJpql = "SELECT COUNT(p) FROM Project p JOIN p.typeProjet t WHERE t.code = :typeCode";

        if (key != null && !key.trim().isEmpty()) {
            String searchCondition = " AND (LOWER(p.nomProjet) LIKE LOWER(:key) " +
                    "OR LOWER(p.description) LIKE LOWER(:key) " +
                    "OR LOWER(p.localisation) LIKE LOWER(:key))";
            jpql += searchCondition;
            countJpql += searchCondition;
        }

        TypedQuery<Project> query = em.createQuery(jpql, Project.class);
        TypedQuery<Long> countQuery = em.createQuery(countJpql, Long.class);

        query.setParameter("typeCode", typeCode);
        countQuery.setParameter("typeCode", typeCode);

        if (key != null && !key.trim().isEmpty()) {
            query.setParameter("key", "%" + key.trim() + "%");
            countQuery.setParameter("key", "%" + key.trim() + "%");
        }

        query.setFirstResult(pageable.getPage() * pageable.getSize());
        query.setMaxResults(pageable.getSize());

        List<Project> projects = query.getResultList();
        Long total = countQuery.getSingleResult();

        return new Page<>(projects, total.intValue(), pageable.getPage(), pageable.getSize());
    }
}
