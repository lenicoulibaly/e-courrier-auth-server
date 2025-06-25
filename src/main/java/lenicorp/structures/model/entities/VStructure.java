package lenicorp.structures.model.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "v_structure")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class VStructure
{

    @Id
    @Column(name = "str_id")
    @EqualsAndHashCode.Include
    private Long strId;

    @Column(name = "str_sigle", length = 50)
    private String strSigle;

    @Column(name = "str_name", length = 255)
    private String strName;

    @Column(name = "str_type_code", length = 10)
    private String strTypeCode;

    @Column(name = "str_type_name", length = 255)
    private String strTypeName;

    @Column(name = "situation_geo", length = 100)
    private String situationGeo;

    @Column(name = "str_address", length = 500)
    private String strAddress;

    @Column(name = "str_tel", length = 50)
    private String strTel;

    @Column(name = "chaine_sigles", length = 1000)
    private String chaineSigles;

    @Column(name = "profondeur")
    private Long strLevel;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "parent_name", length = 255)
    private String parentName;

    @Column(name = "parent_sigle", length = 50)
    private String parentSigle;

    // Méthodes utilitaires existantes
    public String[] getHierarchyLevels()
    {
        return chaineSigles != null ? chaineSigles.split("/") : new String[0];
    }

    public String getParentChain()
    {
        if (chaineSigles != null && chaineSigles.contains("/"))
        {
            int lastSlash = chaineSigles.lastIndexOf("/");
            return chaineSigles.substring(0, lastSlash);
        }
        return null;
    }

    public boolean isRoot()
    {
        return strLevel != null && strLevel == 0;
    }

    public long getHierarchyDepth()
    {
        return strLevel != null ? strLevel : 0;
    }

    public String getDirectParentSigle()
    {
        return parentSigle; // Maintenant directement disponible
    }

    public boolean hasParent()
    {
        return parentId != null;
    }

    public String getFullHierarchyDisplay()
    {
        return chaineSigles != null ? chaineSigles.replace("/", " > ") : strSigle;
    }

}