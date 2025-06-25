package lenicorp.structures.model.entities;

import jakarta.persistence.*;
import lenicorp.types.model.entities.Type;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "structure")
public class Structure
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STRUCTURE_ID_GEN")
    @SequenceGenerator(name = "STRUCTURE_ID_GEN", sequenceName = "STRUCTURE_ID_GEN", initialValue = 1)
    private Long strId;
    private String strName;
    private String strSigle;
    @ManyToOne @JoinColumn(name = "PARENT_ID")
    private Structure strParent;
    @ManyToOne @JoinColumn(name="STR_TYPE_CODE")
    private Type strType;

    private String strTel;
    private String strAddress;
    private String situationGeo;
    private String creationActFilePath;

    @Transient
    private List<Structure> strChildren;

    public Structure(Long strId)
    {
        this.strId = strId;
    }

    @Override
    public String toString()
    {
        return this.strName + " ("+this.strSigle + ")";
    }



}
