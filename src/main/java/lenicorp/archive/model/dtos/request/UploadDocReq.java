package lenicorp.archive.model.dtos.request;

import jakarta.ws.rs.FormParam;
import lenicorp.archive.model.dtos.validator.ValidDocType;
import lenicorp.archive.model.dtos.validator.ValidFileExtension;
import lenicorp.archive.model.dtos.validator.ValidFileSize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.InputStream;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@ValidFileExtension @ValidFileSize
public class UploadDocReq
{
    @FormParam("objectId")
    private Long objectId; //Peut être l'id d'un utilisateur, d'une association, d'une section, ou d'un autre objet futur quand le projet grandira
    @ValidDocType
    @FormParam("docTypeCode")
    private String docTypeCode;
    @FormParam("docNum")
    private String docNum;
    @FormParam("docName")
    private String docName;
    @FormParam("docDescription")
    private String docDescription;
    @FormParam("file")
    private InputStream file;
}
