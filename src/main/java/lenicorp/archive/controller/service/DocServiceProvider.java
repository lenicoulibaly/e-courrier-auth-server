package lenicorp.archive.controller.service;

import jakarta.enterprise.context.ApplicationScoped;
import lenicorp.exceptions.AppException;
import lenicorp.types.controller.repositories.TypeRepo;
import lenicorp.types.model.entities.Type;
import lombok.RequiredArgsConstructor;

@ApplicationScoped @RequiredArgsConstructor
public class DocServiceProvider
{
    private final AssociationDocUploader assDocUploader;
    private final SectionDocUploader sectionDocUploader;
    private final MembreDocUploader membreDocUploader;
    private final PhotoDocUploader photoDocUploader;
    private final TypeRepo typeRepo;

    public AbstractDocumentService getDocUploader(String typeDocUniqueCode)
    {
        if(typeDocUniqueCode == null) throw new AppException("Le type de document ne peut être null");
        Type typeDoc = typeRepo.findByIdOptional(typeDocUniqueCode.toUpperCase()).orElseThrow(()->new AppException("Type de document inconnu"));
        if(typeDoc == null || !"DOC".equals(typeDoc.typeGroup.groupCode)) throw new AppException("Ce type de document n'est pas pris en charge par le système");

        AbstractDocumentService uploader = switch (typeDoc.code)
                {
                    case "PHT"->photoDocUploader;
                    case "DOC_MBR"->membreDocUploader;
                    case "DOC_ASS"->assDocUploader;
                    case "DOC_SEC"->sectionDocUploader;
                    default -> null;
                };

        if(uploader == null) throw new AppException("Ce type de document n'est pas pris en charge par le système");
        return uploader;
    }
}
