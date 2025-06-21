
package lenicorp.types.controller.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import lenicorp.types.model.entities.TypeMapping;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class TypeMappingRepo implements PanacheRepository<TypeMapping>
{
    public Boolean parentHasDirectSousType(String parentCode, String childCode) {
        return count("parent.code = ?1 and child.code = ?2", parentCode.toUpperCase(), childCode.toUpperCase()) > 0;
    }

    public List<String> findSousTypeCodesToRemove(String parentCode, List<String> inputSousTypeCodes) {
        if (inputSousTypeCodes == null || inputSousTypeCodes.isEmpty()) {
            // Si pas de codes en entrée, tous les sous-types actuels doivent être supprimés
            String query = """
                select tm.child.code from TypeMapping tm 
                where tm.parent.code = :parentCode
            """;
            return getEntityManager()
                    .createQuery(query, String.class)
                    .setParameter("parentCode", parentCode.toUpperCase())
                    .getResultList();
        }

        String query = """
            select tm.child.code from TypeMapping tm 
            where tm.parent.code = :parentCode 
            and tm.child.code not in (:inputCodes)
        """;
        return getEntityManager()
                .createQuery(query, String.class)
                .setParameter("parentCode", parentCode.toUpperCase())
                .setParameter("inputCodes", inputSousTypeCodes.stream()
                        .map(String::toUpperCase)
                        .collect(Collectors.toList()))
                .getResultList();
    }

    public List<String> findSousTypeCodesToAdd(String parentCode, List<String> inputSousTypeCodes) {
        if (inputSousTypeCodes == null || inputSousTypeCodes.isEmpty()) {
            return List.of();
        }

        String query = """
            select t.code from Type t 
            where not exists (
                select tm from TypeMapping tm 
                where tm.parent.code = :parentCode 
                and tm.child.code = t.code
            ) 
            and upper(t.code) in (:inputCodes)
        """;
        return getEntityManager()
                .createQuery(query, String.class)
                .setParameter("parentCode", parentCode.toUpperCase())
                .setParameter("inputCodes", inputSousTypeCodes.stream()
                        .map(String::toUpperCase)
                        .collect(Collectors.toList()))
                .getResultList();
    }

    public void removeSousTypes(String parentCode, List<String> sousTypeCodesToRemove) {
        if (sousTypeCodesToRemove == null || sousTypeCodesToRemove.isEmpty()) {
            return;
        }

        String query = """
            delete from TypeMapping tm 
            where tm.parent.code = :parentCode 
            and tm.child.code in (:childCodes)
        """;
        getEntityManager()
                .createQuery(query)
                .setParameter("parentCode", parentCode.toUpperCase())
                .setParameter("childCodes", sousTypeCodesToRemove.stream()
                        .map(String::toUpperCase)
                        .collect(Collectors.toList()))
                .executeUpdate();
    }
}