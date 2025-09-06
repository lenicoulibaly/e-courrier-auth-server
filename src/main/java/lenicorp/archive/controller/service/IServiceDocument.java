package lenicorp.archive.controller.service;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import lenicorp.archive.model.dtos.request.UpdateDocReq;
import lenicorp.archive.model.dtos.request.UploadDocReq;
import lenicorp.archive.model.dtos.response.Base64FileDto;
import lenicorp.archive.model.dtos.response.ReadDocDTO;
import lenicorp.archive.model.entities.Document;
import lenicorp.types.model.dtos.TypeDTO;
import lenicorp.utilities.Page;
import lenicorp.utilities.PageRequest;

import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.List;

public interface IServiceDocument
{
	void uploadFile(InputStream file, String destinationPath) throws RuntimeException;
	byte[] downloadFile(String filePAth);
	Response downloadFile(Long docI);

    @Transactional
	Document uploadDocument(UploadDocReq dto) throws IOException;

	boolean deleteDocument(Long docId) throws UnknownHostException;

	@Transactional
	Document updateDocument(UpdateDocReq dto) throws IOException;

    void displayPdf(HttpServletResponse response, byte[] reportBytes, String displayName)  throws Exception;

    Part downloadMultipartFile(String filePAth);
	public String generatePath(String extension, String typeCode, String objectName) throws IOException;
    boolean deleteFile(String filePath);
	void renameFile(String oldPath, String newPath);

	Page<ReadDocDTO> getAllDocsForObject(Long userId, Long assoId, Long sectionId, String key, PageRequest pageRequest);

	// New methods
	List<TypeDTO> getTypeDocumentReglement(String typeDocUniqueCode);
	Base64FileDto displayDocument(Long docId) throws Exception;
	Document getDocumentById(Long docId);
}
