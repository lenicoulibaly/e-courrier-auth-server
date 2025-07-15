package lenicorp.types.controller.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lenicorp.utilities.Page;
import lenicorp.utilities.PageRequest;
import lenicorp.utilities.StringUtils;
import lenicorp.types.model.dtos.TypeGroupDTO;
import lenicorp.types.model.entities.TypeGroup;
import lenicorp.types.model.mappers.TypeGroupMapper;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class TypeGroupRepo implements PanacheRepositoryBase<TypeGroup, String>
{
    @Inject private TypeGroupMapper typeGroupMapper;

    public Boolean existsByGroupCode(String groupCode)
    {
        if (StringUtils.isBlank(groupCode)) return false;
        return count("upper(groupCode) = ?1", groupCode.toUpperCase()) > 0;
    }

    public Boolean existsByName(String name)
    {
        if (StringUtils.isBlank(name)) return false;
        return count("upper(name) = ?1", name.toUpperCase()) > 0;
    }

    public Boolean existsByName(String name, String groupCode)
    {
        if (StringUtils.isBlank(name)) return false;
        return count("upper(name) = ?1 and upper(groupCode) <> ?2", name.toUpperCase(), groupCode.toUpperCase()) > 0;
    }

    public Page<TypeGroupDTO> searchTypeGroups(String key, PageRequest pageRequest)
    {
        String safeKey = "%" + StringUtils.stripAccentsToUpperCase(key) + "%";
        int page = pageRequest.getPage();
        int size = pageRequest.getSize();
        String baseQuery = """
            from Type_Group tg 
            where 
                unaccent(upper(tg.group_code)) like :key or
                unaccent(upper(tg.name)) like :key
        """;

        String countQuery = "select count(*) " + baseQuery;
        String searchQuery = "select * " + baseQuery + " order by name";

        // Exécution synchrone du count
        Long totalElements = (Long) getEntityManager()
                .createNativeQuery(countQuery, Long.class)
                .setParameter("key", safeKey)
                .getSingleResult();

        // Exécution synchrone de la recherche
        @SuppressWarnings("unchecked")
        List<TypeGroup> resultList = getEntityManager()
                .createNativeQuery(searchQuery, TypeGroup.class)
                .setParameter("key", safeKey)
                .setFirstResult(page * size)
                .setMaxResults(size)
                .getResultList();

        List<TypeGroupDTO> content = resultList.stream()
                .map(typeGroupMapper::mapToDto)
                .collect(Collectors.toList());

        return new Page<>(content, totalElements, page, size);
    }

    public String findGroupCodeByTypeCode(String typeCode)
    {
        if (StringUtils.isBlank(typeCode)) {
            return null;
        }

        String query = """
            select t.typeGroup.groupCode from Type t 
            where upper(t.code) = :typeCode
        """;

        try {
            return getEntityManager()
                    .createQuery(query, String.class)
                    .setParameter("typeCode", typeCode.toUpperCase())
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public List<TypeGroupDTO> getAllTypeGroupes()
    {
        String query = "select new lenicorp.types.model.dtos.TypeGroupDTO(tg.groupCode, tg.name) from TypeGroup tg";
        return getEntityManager().createQuery(query, TypeGroupDTO.class).getResultList().stream().collect(Collectors.toList());
    }
}