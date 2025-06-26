package lenicorp.structures.controller.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lenicorp.structures.model.dtos.ReadStrDTO;
import lenicorp.structures.model.dtos.StrMapper;
import lenicorp.structures.model.entities.VStructure;
import lenicorp.utilities.Page;
import lenicorp.utilities.PageRequest;
import lenicorp.utilities.StringUtils;

import java.util.Collections;
import java.util.List;

@ApplicationScoped
public class VStrRepo implements PanacheRepository<VStructure>
{
    @Inject private StrMapper strMapper;

    public List<ReadStrDTO> findAllDescendants(Long strId)
    {
        String parentChaineSigles = find("strId", strId).firstResult().getChaineSigles();

        if (parentChaineSigles == null)  return Collections.emptyList();

        // Deuxième requête pour trouver tous les descendants
        List<VStructure> descendants = find("chaineSigles like ?1 and strId != ?2 order by chaineSigles", parentChaineSigles + "/%", strId).list();

        return strMapper.mapToReadStrDTOList(descendants);
    }


    public String getChaineSigles(Long strId)
    {
        return find("strId", strId).firstResult().getChaineSigles();
    }

    public Page<ReadStrDTO> search(String key, Long parentId, String typeCode, PageRequest pageRequest)
    {
        String safeKey = "%" + StringUtils.stripAccentsToUpperCase(key) + "%";
        int page = pageRequest.getPage();
        int size = pageRequest.getSize();
        String parentChaineSigles = parentId == null ? null : getChaineSigles(parentId);

        String baseQuery = """
                from VStructure vs 
                where 
                    vs.strTypeCode = coalesce(:typeCode, vs.strTypeCode) 
                    and (vs.strName like :key or vs.strSigle like :key)
                """;
        if (parentChaineSigles != null) baseQuery += " and vs.chaineSigles like :parentChaineSigles";

        String countQuery = "select count(vs.strId) " + baseQuery;
        String selectQuery = "select vs " + baseQuery + " order by vs.chaineSigles";
        
         var countQueryExecutor = getEntityManager().createQuery(countQuery, Long.class)
                .setParameter("key", safeKey)
                .setParameter("typeCode", typeCode);
        if(parentChaineSigles != null) countQueryExecutor.setParameter("parentChaineSigles", parentChaineSigles + "%");
        Long totalEments = countQueryExecutor.getSingleResult();

        var selectQueryExecutor = getEntityManager().createQuery(selectQuery, VStructure.class)
            .setParameter("key", safeKey)
            .setParameter("typeCode", typeCode);

        if(parentChaineSigles != null) selectQueryExecutor.setParameter("parentChaineSigles", parentChaineSigles+ "%");
        List<VStructure> content = selectQueryExecutor.setMaxResults(size).setFirstResult(page * size).getResultList();

        List<ReadStrDTO> readStrDTOList = strMapper.mapToReadStrDTOList(content);

        return new Page<>(readStrDTOList, totalEments, page, size);
    }
}