package lenicorp.archive.controller.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import lenicorp.archive.model.dtos.request.UploadDocReq;
import lenicorp.archive.model.entities.Document;

@ApplicationScoped
@Named("section")
public class SectionDocUploader extends AbstractDocumentService
{
	@Override
	protected Document mapToDocument(UploadDocReq dto) {
		return docMapper.mapToAssociationDoc(dto);
	}
}
