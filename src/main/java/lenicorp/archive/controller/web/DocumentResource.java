package lenicorp.archive.controller.web;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lenicorp.archive.controller.service.AbstractDocumentService;
import lenicorp.archive.controller.service.DocServiceProvider;
import lenicorp.archive.model.dtos.request.UpdateDocReq;
import lenicorp.archive.model.dtos.request.UploadDocReq;
import lenicorp.archive.model.dtos.response.Base64FileDto;
import lenicorp.archive.model.dtos.response.ReadDocDTO;
import lenicorp.archive.model.entities.Document;
import lenicorp.types.model.dtos.TypeDTO;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Path("/documents")
//@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
public class DocumentResource {

    @Inject
    DocServiceProvider docServiceProvider;

    @Inject AbstractDocumentService docService;

    @GET
    @Path("/{typeDocUniqueCode}/types")
    public List<TypeDTO> getTypeDocumentReglement(@PathParam("typeDocUniqueCode") String typeDocUniqueCode)
    {
        AbstractDocumentService docService = docServiceProvider.getDocUploader(typeDocUniqueCode);
        return docService.getTypeDocumentReglement(typeDocUniqueCode);
    }

    @POST
    @Path("/{groupDocUniqueCode}/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Document uploadDocument(@MultipartForm UploadDocReq dto,
            @PathParam("groupDocUniqueCode") String groupDocUniqueCode) throws IOException
    {
        groupDocUniqueCode = groupDocUniqueCode == null || groupDocUniqueCode.trim().equals("") 
            ? ""
            : groupDocUniqueCode.replace("-", "_").toUpperCase();

        AbstractDocumentService docUploader = docServiceProvider.getDocUploader(groupDocUniqueCode);
        //UploadDocReq dto = new UploadDocReq(objectId, docTypeCode, docNum, docName, docDescription, file);
        Document doc = docUploader.uploadDocument(dto);
        return doc;
    }

    @PUT
    @Path("/update")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Document updateDocument(@MultipartForm UpdateDocReq dto) throws IOException {

        //UpdateDocReq dto = new UpdateDocReq(docId, docUniqueCode, docNum, docName, docDescription, file);
        Document doc = docService.updateDocument(dto);
        return doc;
    }

    @DELETE
    @Path("/delete/{docId}")
    public Response deleteDocument(@PathParam("docId") Long docId) throws IOException
    {
        boolean result = docService.deleteDocument(docId);
        return Response.ok(result).build();
    }

    @GET
    @Path("/users/{userId}")
    public Response getUserDocs(
            @PathParam("userId") Long userId,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size,
            @QueryParam("key") @DefaultValue("") String key) {
        // For now, return an empty list
        // In a real implementation, you would query the database for documents
        return Response.ok(new ArrayList<ReadDocDTO>()).build();
    }

    @GET
    @Path("/associations/{assoId}")
    public Response getAssociationDocs(
            @PathParam("assoId") Long assoId,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size,
            @QueryParam("key") @DefaultValue("") String key) {
        // For now, return an empty list
        // In a real implementation, you would query the database for documents
        return Response.ok(new ArrayList<ReadDocDTO>()).build();
    }

    @GET
    @Path("/sections/{sectionId}")
    public Response getSectionDocs(
            @PathParam("sectionId") Long sectionId,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size,
            @QueryParam("key") @DefaultValue("") String key) {
        // For now, return an empty list
        // In a real implementation, you would query the database for documents
        return Response.ok(new ArrayList<ReadDocDTO>()).build();
    }

    @GET
    @Path("/get-base64/{docId}")
    public Base64FileDto displayDocument(@PathParam("docId") Long docId) throws Exception {
        return docService.displayDocument(docId);
    }

    @GET
    @Path("/download/{docId}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response downloadFile(@PathParam("docId") Long docId) {
        Document doc = docService.getDocumentById(docId);
        byte[] fileBytes = docService.downloadFile(doc.getDocPath());

        return Response.ok(fileBytes)
                .header("Content-Disposition", "attachment; filename=\"" + doc.getDocName() + "\"")
                .header("Content-Type", doc.getDocMimeType())
                .build();
    }
}
