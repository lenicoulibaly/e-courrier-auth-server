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

import java.util.List;

@ApplicationScoped
public class VStrRepo implements PanacheRepository<VStructure>
{
    @Inject private StrMapper strMapper;
    /**
     * Trouve toutes les structures d'un type donné
     */
    public List<VStructure> findByTypeCode(String typeCode) {
        return find("strTypeCode", typeCode).list();
    }


    /**
     * Trouve toutes les structures racines (ministères)
     */
    public List<VStructure> findRootStructures() {
        return find("profondeur", 0).list();
    }

    /**
     * Trouve toutes les directions générales
     */
    public List<VStructure> findDirectionsGenerales() {
        return findByTypeCode("DG");
    }

    /**
     * Trouve les structures contenant une chaîne dans leur hiérarchie
     */
    public List<VStructure> findByChainContaining(String partialChain) {
        return find("chaineSigles like ?1", "%" + partialChain + "%").list();
    }

    /**
     * Trouve les enfants directs d'une structure donnée
     */
    public List<VStructure> findDirectChildren(String parentSigle) {
        return find("chaineSigles like ?1 and profondeur = (select s.profondeur from VStructure s where s.strSigle = ?2) + 1", 
                   parentSigle + "/%", parentSigle).list();
    }

    /**
     * Trouve tous les descendants d'une structure donnée
     */
    public List<VStructure> findAllDescendants(String ancestorSigle)
    {
        return find("chaineSigles like ?1", ancestorSigle + "/%").list();
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
                    and vs.chaineSigles like concat(:parentChaineSigles, '%')
                    and (vs.strName like :key or vs.strSigle like :key)
                """;
        String countQuery = "select count(vs.strId) " + baseQuery;
        String selectQuery = "select vs " + baseQuery + " order by vs.chaineSigles";
        
        Long totalEments = getEntityManager().createQuery(countQuery, Long.class)
                .setParameter("key", safeKey)
                .setParameter("typeCode", typeCode)
                .setParameter("parentChaineSigles", parentChaineSigles)
                .getSingleResult();

        List<VStructure> content = getEntityManager().createQuery(selectQuery, VStructure.class)
            .setParameter("key", safeKey)
            .setParameter("typeCode", typeCode)
            .setParameter("parentChaineSigles", parentChaineSigles)
                .setMaxResults(size).setFirstResult(page * size).getResultList();

        List<ReadStrDTO> readStrDTOList = strMapper.mapToReadStrDTOList(content);

        return new Page<>(readStrDTOList, totalEments, page, size);
    }
}