package lenicorp.project.controller.services;

import lenicorp.project.model.dtos.CreateProjectDTO;
import lenicorp.project.model.dtos.ReadProjectDTO;
import lenicorp.project.model.dtos.UpdateProjectDTO;
import lenicorp.project.model.entities.Project;
import lenicorp.utilities.Page;
import lenicorp.utilities.PageRequest;

import java.io.IOException;

public interface IProjectService
{
    Project createProject(CreateProjectDTO dto) throws IOException;
    Project updateProject(UpdateProjectDTO dto);
    Page<ReadProjectDTO> searchProjects(String key, PageRequest pageable);
    ReadProjectDTO findById(Long projetId);

    // Additional methods for specific project types
    Page<ReadProjectDTO> searchLandSaleProjects(String key, PageRequest pageable);
    Page<ReadProjectDTO> searchHousingAcquisitionProjects(String key, PageRequest pageable);
    Page<ReadProjectDTO> searchRepayableLoanProjects(String key, PageRequest pageable);

    // Method to generate project report
    byte[] generateProjectReport(Long projetId) throws Exception;
}
