package lenicorp.association.controller.services;

import lenicorp.association.model.dtos.CreateAssociationDTO;
import lenicorp.association.model.dtos.ReadAssociationDTO;
import lenicorp.association.model.dtos.UpdateAssociationDTO;
import lenicorp.association.model.entities.Association;
import lenicorp.utilities.Page;
import lenicorp.utilities.PageRequest;

import java.io.File;
import java.io.IOException;

public interface IAssociationService
{
    Association createAssociation(CreateAssociationDTO dto) throws IOException;
    Association updateAssociation(UpdateAssociationDTO dto);
    Page<ReadAssociationDTO> searchAssociations(String key, PageRequest pageable);

    ReadAssociationDTO findById(Long assoId);

    Association createAssociation(CreateAssociationDTO dto, File logo) throws IOException;
    byte[] generateFicheAdhesion(Long assoId) throws Exception;
}
