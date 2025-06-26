package lenicorp.structures.controller.repositories;

import jakarta.enterprise.context.ApplicationScoped;
import lenicorp.structures.model.dtos.ChangeAnchorDTO;
import lenicorp.structures.model.dtos.CreateOrUpdateStrDTO;
import lenicorp.structures.model.dtos.ReadStrDTO;
import lenicorp.utilities.StringUtils;

import java.util.List;

@ApplicationScoped
public class StrRepo implements IStrRepo
{
    public boolean parentHasCompatibleSousType(Long strParentId, String childTypeCode)
    {
        if (strParentId == null || StringUtils.isBlank(childTypeCode))
        {
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
        } else
        {
            requete += " and s.strParent is null";
        }
        if (excludeStrId != null) requete += " and s.strId <> :excludeStrId";

        var query = getEntityManager().createQuery(requete, Long.class).setParameter("sigle", sigle);
        if (parentId != null) query = query.setParameter("parentId", parentId);
        if (excludeStrId != null) query = query.setParameter("excludeStrId", excludeStrId);
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
        } else
        {
            requete += " and s.strParent is null";
        }
        if (excludeStrId != null) requete += " and s.strId <> :excludeStrId";

        var query = getEntityManager().createQuery(requete, Long.class).setParameter("strName", strName);
        if (parentId != null) query = query.setParameter("parentId", parentId);
        if (excludeStrId != null) query = query.setParameter("excludeStrId", excludeStrId);
        var count = query.getSingleResult();
        return count > 0;
    }

    public List<ReadStrDTO> getPossibleParentStructures(String childTypeCode)
    {
        String query;
        if (childTypeCode == null)
        {
            return List.of();
        }

        query = """
                    SELECT new lenicorp.structures.model.dtos.ReadStrDTO(
                            s.strId, s.strName, s.strTypeName, s.strSigle, s.strTypeCode,
                            s.strTel, s.strAddress, situationGeo, s.parentId, s.parentName,
                            s.parentSigle, s.strLevel, s.chaineSigles)
                    FROM VStructure s
                    WHERE s.strTypeCode IN (
                        SELECT tm.parent.code
                        FROM TypeMapping tm
                        WHERE tm.child.code = :childTypeCode
                    )
                """;

        return getEntityManager()
                .createQuery(query, ReadStrDTO.class)
                .setParameter("childTypeCode", childTypeCode)
                .getResultList();
    }

    public List<ReadStrDTO> getRootStructures()
    {
        String query;
        query = """
                    SELECT new lenicorp.structures.model.dtos.ReadStrDTO(
                        s.strId, s.strName, s.strTypeName, s.strSigle, s.strTypeCode,
                        s.strTel, s.strAddress, situationGeo, s.parentId, s.parentName,
                        s.parentSigle, s.strLevel, s.chaineSigles)
                    FROM VStructure s
                    WHERE s.parentId IS NULL
                """;
        return getEntityManager()
                .createQuery(query, ReadStrDTO.class)
                .getResultList();
    }

    @Override
    public CreateOrUpdateStrDTO getUpdateDto(Long strId)
    {
        String query = """
                SELECT new lenicorp.structures.model.dtos.CreateOrUpdateStrDTO(
                    s.strId, s.strName, s.strSigle, s.strType.code, s.strParent.strId,
                    s.strTel, s.strAddress, s.situationGeo)
                FROM Structure s
                WHERE s.strId = :strId
                """;

        return getEntityManager()
                .createQuery(query, CreateOrUpdateStrDTO.class)
                .setParameter("strId", strId)
                .getSingleResult();
    }

    @Override
    public ChangeAnchorDTO getChangeAnchorDto(Long strId)
    {
        String query = """
                SELECT new lenicorp.structures.model.dtos.ChangeAnchorDTO(
                    s.strId, s.strType.code, s.strParent.strId, s.strName, s.strSigle,
                    s.strTel, s.strAddress, s.situationGeo)
                FROM Structure s
                WHERE s.strId = :strId
                """;

        return getEntityManager()
                .createQuery(query, ChangeAnchorDTO.class)
                .setParameter("strId", strId)
                .getSingleResult();
    }

}
