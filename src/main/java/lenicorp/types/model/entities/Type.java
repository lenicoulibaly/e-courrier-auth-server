package lenicorp.types.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "type")
@NoArgsConstructor @AllArgsConstructor
public class Type
{
    @Id
    public String code;
    @Column(length = 100, nullable = false)
    public String name;
    @Column(columnDefinition = "INT DEFAULT 0")
    public int ordre;
    @ManyToOne @JoinColumn(name = "group_code")
    public TypeGroup typeGroup;
    @Column(length = 255)
    public String description;

    public Type(String code)
    {
        this.code = code;
    }
}