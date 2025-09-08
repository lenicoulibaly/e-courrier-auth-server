package lenicorp.paiement.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ReadEcheancierDTO
{
    private String typeEcheancierCode;
    private String typeEcheancierName;
    private String frequenceTypeCode;
    private String frequenceTypeName;
    private String name;
    private Long exeCode;
    List<ReadEcheanceDTO2> echeances;

    public ReadEcheancierDTO(String typeEcheancierCode, String typeEcheancierName, String frequenceTypeCode, String frequenceTypeName, String name, Long exeCode) {
        this.typeEcheancierCode = typeEcheancierCode;
        this.typeEcheancierName = typeEcheancierName;
        this.frequenceTypeCode = frequenceTypeCode;
        this.frequenceTypeName = frequenceTypeName;
        this.name = name;
        this.exeCode = exeCode;
    }
}
