package lenicorp.types.model.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "type_group")
@NoArgsConstructor @AllArgsConstructor
public class TypeGroup
{
    @Id
    public String groupCode;
    public String name;
    public TypeGroup(String groupCode) {
        this.groupCode = groupCode;
    }
}
