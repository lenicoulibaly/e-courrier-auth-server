package lenicorp.archive.model.dtos.request;

import jakarta.ws.rs.FormParam;
import lenicorp.archive.model.dtos.validator.ExistingDocId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.InputStream;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor

public class UpdateDocReq
{
    @ExistingDocId
    @FormParam("docId")
    private Long docId;
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

    public UpdateDocReq(Long docId, String docTypeCode, String docNum, String docDescription, InputStream file)
    {
        this.docId = docId;
        this.docTypeCode = docTypeCode;
        this.docNum = docNum;
        this.docDescription = docDescription;
        this.file = file;
    }
}
