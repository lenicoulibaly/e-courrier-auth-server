package lenicorp.archive.model.dtos;

import jakarta.inject.Inject;
import lenicorp.archive.model.dtos.request.UploadDocReq;
import lenicorp.archive.model.entities.Document;
import lenicorp.association.model.entities.Association;
import lenicorp.association.model.entities.Section;
import lenicorp.exceptions.AppException;
import lenicorp.security.model.entities.AppUser;
import lenicorp.types.controller.repositories.TypeRepo;
import lenicorp.types.model.entities.Type;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "cdi")
public abstract class DocMapper
{
    @Inject protected TypeRepo typeRepo;

    protected Type mapDocType(String typeCode)
    {
        return typeRepo.findByIdOptional(typeCode).orElseThrow(() -> new AppException("Type de document inconnu"));
    }


    protected AppUser mapToUser(Long userId)
    {
        return new AppUser(userId);
    }


    protected Association mapToAssociation(Long assoId)
    {
        return new Association(assoId);
    }

    protected Section mapToSection(Long sectionId)
    {
        return new Section(sectionId);
    }

    @Mapping(target = "docDescription", expression = "java(\"Photo de profil\")")
    @Mapping(target = "docType", expression = "java(mapDocType(dto.getDocTypeCode()))")
    @Mapping(target = "user", expression = "java(mapToUser(dto.getObjectId()))")
    public abstract Document mapToPhotoDoc(UploadDocReq dto);

    @Mapping(target = "docType", expression = "java(mapDocType(dto.getDocTypeCode()))")
    @Mapping(target = "association", expression = "java(mapToAssociation(dto.getObjectId()))")
    public abstract Document mapToAssociationDoc(UploadDocReq dto);

    @Mapping(target = "docType", expression = "java(mapDocType(dto.getDocTypeCode()))")
    @Mapping(target = "section", expression = "java(mapToSection(dto.getObjectId()))")
    public abstract Document mapToSectionDoc(UploadDocReq dto);

    @Mapping(target = "docType", expression = "java(mapDocType(dto.getDocTypeCode()))")
    @Mapping(target = "user", expression = "java(mapToUser(dto.getObjectId()))")
    public abstract Document mapToMembreDoc(UploadDocReq dto);

    @Mapping(target = "docType", expression = "java(mapDocType(dto.getDocTypeCode()))")
    public abstract Document mapToDoc(UploadDocReq dto);
}
