package lenicorp.association.controller.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import lenicorp.archive.controller.service.AbstractDocumentService;
import lenicorp.archive.model.dtos.request.UploadDocReq;
import lenicorp.association.controller.repositories.spec.IAssoRepo;
import lenicorp.association.model.dtos.CreateAssociationDTO;
import lenicorp.association.model.dtos.CreateSectionDTO;
import lenicorp.association.model.dtos.ReadAssociationDTO;
import lenicorp.association.model.dtos.UpdateAssociationDTO;
import lenicorp.association.model.entities.Association;
import lenicorp.association.model.mappers.AssoMapper;
import lenicorp.exceptions.AppException;
import lenicorp.utilities.Page;
import lenicorp.utilities.PageRequest;
import lenicorp.utilities.StringUtils;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@ApplicationScoped @RequiredArgsConstructor
public class AssociationService implements IAssociationService
{
    @Inject
    IAssoRepo assoRepo;

    @Inject
    AssoMapper assoMapper;

    @Inject
    ISectionService sectionService;

    @Inject() @Named("association") AbstractDocumentService assoDocService;

    // These dependencies will need to be updated once the corresponding services are migrated to Quarkus
    // For now, we'll comment them out to make the service compile
    /*
    @Inject
    DocumentRepository docRepo;

    @Inject
    AbstractDocumentService documentService;

    @Inject
    IReportService reportService;

    @Inject
    IResourceLoader resourceLoader;
    */

    @Override
    @Transactional
    public Association createAssociation(CreateAssociationDTO dto) throws IOException
    {
        Association association = assoMapper.mapToAssociation(dto);
        assoRepo.persist(association);
        Long assoId = association.getAssoId();

        List<CreateSectionDTO> createSectionDTOS = dto.getCreateSectionDTOS();

        if (createSectionDTOS == null || createSectionDTOS.isEmpty())
        {
            sectionService.createSectionDeBase(association);
        } else
        {
            createSectionDTOS.stream()
                    .filter(Objects::nonNull)
                    .forEach(createSectionDTO ->
                    {
                        createSectionDTO.setAssoId(assoId);
                        sectionService.createSection(createSectionDTO);
                    });
        }
        if(dto.getLogo() != null)
        {
            UploadDocReq docReq = new UploadDocReq(assoId, "LOGO", null, "Logo", "Logo de l'association", dto.getLogo());
            assoDocService.uploadDocument(docReq);
        }
        return association;
    }

    @Override @Transactional
    public Association updateAssociation(UpdateAssociationDTO dto)
    {
        Association association = assoRepo.findById(dto.getAssoId());
        if (association == null) {
            throw new AppException("Association introuvable");
        }

        association.setAssoName(dto.getAssoName());
        association.setSigle(dto.getSigle());
        association.setDroitAdhesion(dto.getDroitAdhesion());
        association.setSituationGeo(dto.getSituationGeo());
        return association;
    }

    @Override
    public Page<ReadAssociationDTO> searchAssociations(String key, PageRequest pageRequest)
    {
        key = StringUtils.stripAccentsToUpperCase(key);
        return assoRepo.searchAssociations(key, pageRequest);

        // The following code is commented out because the document-related services are not yet migrated to Quarkus
        /*
        Page<ReadAssociationDTO> assoPage = assoRepo.searchAssociations(key, pageRequest);
        List<ReadAssociationDTO> assoList = assoPage.getContent();
        assoList.forEach(a->
        {
            ReadDocDTO logo = docRepo.getAssoLogo(a.getAssoId());
            if(logo != null) logo.setFile(documentService.downloadFile(logo.getDocPath()));
            a.setLogo(logo);
        });
        return new Page<>(assoList, assoPage.getTotalElements(), assoPage.getPage(), assoPage.getSize());
        */
    }

    @Override
    public ReadAssociationDTO findById(Long assoId)
    {
        if(assoId == null) throw new AppException("L'ID de l'association ne peut être nul");
        return assoRepo.findReadAssoDtoById(assoId);

        // The following code is commented out because the document-related services are not yet migrated to Quarkus
        /*
        ReadAssociationDTO assoDTO = assoRepo.findReadAssoDtoById(assoId);
        ReadDocDTO logo = docRepo.getAssoLogo(assoId);
        assoDTO.setLogo(logo);
        return assoDTO;
        */
    }

    @Override
    public Association createAssociation(CreateAssociationDTO dto, File logo) throws IOException
    {
        // Simple implementation that ignores the logo for now
        // This will need to be updated once the document-related services are migrated to Quarkus
        return this.createAssociation(dto);

        /*
        Association association = this.createAssociation(dto);
        // Code to handle the logo file would go here
        return association;
        */
    }

    @Override
    public byte[] generateFicheAdhesion(Long assoId) throws Exception {
        // This is a stub implementation that will need to be updated
        // once the report-related services are migrated to Quarkus
        throw new AppException("Cette fonctionnalité n'est pas encore disponible");

        /*
        Map<String, Object> parameters = new HashMap<>();

        String assoSigle = assoRepo.getSigleByAssoId(assoId);
        String qrText = "Fiche d'adhésion " + assoSigle;
        parameters.put("ASSO_ID", assoId);

        ReadDocDTO logoDoc = docRepo.getAssoLogo(assoId);
        if(logoDoc != null)
        {
            InputStream logo = resourceLoader.getLocalImages(logoDoc.getDocPath());
            parameters.put("LOGO", logo);
        }

        return reportService.generateReport("FicheAdhesion.jrxml", parameters, Collections.EMPTY_LIST, qrText);
        */
    }
}
