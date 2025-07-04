
package lenicorp.types.controller.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import lenicorp.types.model.dtos.TypeDTO;
import lenicorp.types.model.entities.Type;
import lenicorp.types.model.mappers.TypeMapper;
import lenicorp.utilities.Page;
import lenicorp.utilities.PageRequest;
import lenicorp.utilities.StringUtils;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped @RequiredArgsConstructor
public class TypeRepo implements PanacheRepositoryBase<Type, String>
{
    private final TypeMapper typeMapper;

    public Boolean existsByCode(String code)
    {
        if(StringUtils.isBlank(code)) return false;
        return this.count("upper(code) = ?1", code.toUpperCase()) > 0;
    }

    public Boolean existsByName(String name)
    {
        if(StringUtils.isBlank(name)) return false;
        return this.count("upper(name) = ?1", name.toUpperCase()) > 0;
    }

    public Boolean existsByName(String name, String code)
    {
        if(StringUtils.isBlank(name)) return false;
        return this.count("upper(name) = ?1 and upper(code) <> ?2", name.toUpperCase(), code.toUpperCase()) > 0;
    }

    public List<TypeDTO> findDirectSousTypes(String parentCode)
    {
        String query = """
                select tm.child from TypeMapping tm
                where tm.parent.code = :parentCode
            """;
        List<Type> types = getEntityManager()
                .createQuery(query, Type.class)
                .setParameter("parentCode", parentCode.toUpperCase())
                .getResultList();

        return types.stream()
                .map(typeMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public Page<TypeDTO> searchTypes(String key, List<String> groupCodes, PageRequest pageRequest)
    {
        int page = pageRequest.getPage();
        int size = pageRequest.getSize();
        String safeKey = "%" + StringUtils.stripAccentsToUpperCase(key) + "%";
        boolean hasGroupFilter = groupCodes != null && !groupCodes.isEmpty();

        String baseQuery = """
        from Type t
        where
        (
            unaccent(upper(t.code)) like unaccent(upper(:key)) or
            unaccent(upper(t.name)) like unaccent(upper(:key)) or
            unaccent(upper(t.description)) like unaccent(upper(:key))
        )
        """ + (hasGroupFilter ? " and t.group_code in :groupCodes" : "");

        String countQuery = "select count(t.code) " + baseQuery;
        String selectQuery = "select * " + baseQuery + " order by group_code, name";

        // Exécution synchrone du count
        var countQueryObj = getEntityManager().createNativeQuery(countQuery, Long.class)
                .setParameter("key", safeKey);
        if(hasGroupFilter) countQueryObj.setParameter("groupCodes", groupCodes);
        Long totalElements = (Long) countQueryObj.getSingleResult();

        // Exécution synchrone de la requête de contenu
        var selectQueryObj = getEntityManager().createNativeQuery(selectQuery, Type.class)
                .setParameter("key", safeKey)
                .setFirstResult(page * size)
                .setMaxResults(size);
        if(hasGroupFilter) selectQueryObj.setParameter("groupCodes", groupCodes);

        @SuppressWarnings("unchecked")
        List<Type> resultList = selectQueryObj.getResultList();
        List<TypeDTO> content = resultList.stream()
                .map(typeMapper::mapToDto)
                .collect(Collectors.toList());

        return new Page<>(content, totalElements, page, size);
    }

    public List<TypeDTO> findByGroupCode(String groupCode)
    {
        groupCode = StringUtils.stripAccentsToUpperCase(groupCode);
        if(StringUtils.isBlank(groupCode)) return List.of();

        List<Type> types = this.list("upper(typeGroup.groupCode) = ?1", groupCode);
        return types.stream()
                .map(typeMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public List<Type> findByTypeGroup(String groupCode)
    {
        return this.list("upper(typeGroup.groupCode) = ?1", StringUtils.stripAccentsToUpperCase(groupCode));
    }


    public List<String> getParentTypeCodes(String typeCode)
    {
        if (StringUtils.isBlank(typeCode)) return List.of();

        String query = "select tm.parent.code from TypeMapping tm where upper(tm.child.code) = :typeCode";
        return getEntityManager()
                .createQuery(query, String.class)
                .setParameter("typeCode", StringUtils.stripAccentsToUpperCase(typeCode))
                .getResultList();
    }

    public boolean existsByCodeAndGroupCode(String typeCode, String groupCode)
    {
        if (StringUtils.isBlank(typeCode) || StringUtils.isBlank(groupCode)) return false;
        return this.count("upper(code) = upper(?1) and upper(typeGroup.groupCode) = upper(?2)", typeCode, groupCode) > 0;
    }
}