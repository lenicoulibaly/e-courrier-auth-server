package lenicorp.structures.controller.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.constraints.NotNull;
import lenicorp.structures.model.entities.Structure;
import lenicorp.utilities.StringUtils;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@ApplicationScoped
public class StrRepo implements PanacheRepository<Structure>
{
    List<Structure> findByStrParent(Long strId)
    {
        return find("strParent.strId = ?1", strId).list();
    }

    Structure getStrParent(long strId)
    {
        return find("strId = ?1", strId).firstResult().getStrParent();
    }

    List<Structure> findByStrLevel(long strLevel)
    {
        return find("strLevel = ?1", strLevel).list();
    }

    List<Structure> findByTypeCodeIn(List<String> typeCodes)
    {
        if (typeCodes == null || typeCodes.isEmpty()) return List.of();
        String query = "select s from Structure s join s.strType t where t.code in (:typeCodes)";
        return getEntityManager()
                .createQuery(query, Structure.class)
                .setParameter("typeCodes", typeCodes)
                .getResultList();
    }


    public boolean parentHasCompatibleSousType(Long strParentId, String childTypeCode)
    {
        if (strParentId == null || StringUtils.isBlank(childTypeCode)) {
            return false;
        }

        String query = """
        SELECT COUNT(tm) > 0
        FROM TypeMapping tm
        WHERE tm.parent.code = (
            SELECT s.strType.code 
            FROM Structure s 
            WHERE s.strId = :strParentId
        )
        AND UPPER(tm.child.code) = UPPER(:childTypeCode)
    """;

        return getEntityManager()
                .createQuery(query, Boolean.class)
                .setParameter("strParentId", strParentId)
                .setParameter("childTypeCode", childTypeCode.toUpperCase())
                .getSingleResult();
    }

    public boolean sigleExistsUnderSameParent(String sigle, Long parentId, Long excludeStrId)
    {
        if (StringUtils.isBlank(sigle)) return false;

        String requete = "select count(s) from Structure s where upper(s.strSigle) = upper(:sigle)";
        if (parentId != null)
        {
            requete += " and s.strParent.strId = :parentId";
        }
        else
        {
            requete += " and s.strParent is null";
        }
        if(excludeStrId != null) requete += " and s.strId <> :excludeStrId";

        var query = getEntityManager().createQuery(requete, Long.class).setParameter("sigle", sigle);
        if(parentId != null) query = query.setParameter("parentId", parentId);
        if(excludeStrId != null) query = query.setParameter("excludeStrId", excludeStrId);
        var count = query.getSingleResult();
        return count > 0;
    }

    public boolean existsById(Long strId)
    {
        return strId != null && this.count("strId = ?1", strId) > 0;
    }

    public boolean strNameExistsUnderSameParent(String strName, Long parentId, Long excludeStrId)
    {
        if (StringUtils.isBlank(strName)) return false;

        String requete = "select count(s) from Structure s where upper(s.strName) = upper(:strName)";
        if (parentId != null)
        {
            requete += " and s.strParent.strId = :parentId";
        }
        else
        {
            requete += " and s.strParent is null";
        }
        if(excludeStrId != null) requete += " and s.strId <> :excludeStrId";

        var query = getEntityManager().createQuery(requete, Long.class).setParameter("strName", strName);
        if(parentId != null) query = query.setParameter("parentId", parentId);
        if(excludeStrId != null) query = query.setParameter("excludeStrId", excludeStrId);
        var count = query.getSingleResult();
        return count > 0;
    }
}
