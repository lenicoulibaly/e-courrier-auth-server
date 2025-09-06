package lenicorp.archive.controller.service;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.inject.Named;
import lenicorp.archive.model.dtos.request.UploadDocReq;
import lenicorp.archive.model.entities.Document;

@ApplicationScoped
@Named("generic")
@Priority(1) @Alternative
public class GenericDocUploader extends AbstractDocumentService
{
	@Override
	protected Document mapToDocument(UploadDocReq dto)
	{
		return docMapper.mapToDoc(dto);
	}
}