package lenicorp.archive.controller.service;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import lenicorp.archive.model.dtos.request.UploadDocReq;
import lenicorp.archive.model.entities.Document;

@ApplicationScoped
@Named("photo")
@Priority(1)
public class PhotoDocUploader extends AbstractDocumentService
{
	@Override
	protected Document mapToDocument(UploadDocReq dto) {
		return docMapper.mapToPhotoDoc(dto);
	}
}
