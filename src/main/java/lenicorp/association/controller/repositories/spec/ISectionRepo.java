package lenicorp.association.controller.repositories.spec;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import lenicorp.association.model.dtos.ReadSectionDTO;
import lenicorp.association.model.entities.Section;
import lenicorp.utilities.Page;
import lenicorp.utilities.PageRequest;

import java.util.List;

public interface ISectionRepo extends PanacheRepository<Section>
{
    Page<ReadSectionDTO> searchSections(String key, Long assoId, Long strId, PageRequest pageRequest);
    boolean existsByNameAndAssoId(String sectionName, Long assoId);
    List<ReadSectionDTO> findbyAssoId(Long assoId);
    boolean existsById(Long id);
}
