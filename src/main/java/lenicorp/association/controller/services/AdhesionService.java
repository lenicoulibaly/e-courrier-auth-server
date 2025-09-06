package lenicorp.association.controller.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lenicorp.association.controller.repositories.spec.IAdhesionRepo;
import lenicorp.association.model.dtos.AdhesionDTO;
import lenicorp.association.model.entities.Adhesion;
import lenicorp.association.model.entities.Association;
import lenicorp.association.model.entities.Section;
import lenicorp.association.model.mappers.AdhesionMapper;
import lenicorp.exceptions.AppException;
import lenicorp.security.controller.repositories.spec.IUserRepo;
import lenicorp.security.model.entities.AppUser;
import lenicorp.utilities.Page;
import lenicorp.utilities.PageRequest;
import lenicorp.utilities.SelectOption;
import lenicorp.utilities.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class AdhesionService implements IAdhesionService
{
    @Inject
    AdhesionMapper adhesionMapper;

    @Inject
    IAdhesionRepo adhesionRepo;

    @Inject
    IUserRepo userRepo;

    @Override
    @Transactional
    public Adhesion createUserAndAdhesion(AdhesionDTO dto)
    {
        if(userRepo.existsByEmail(dto.getEmail())) throw new AppException("Email déjà attribué " + dto.getEmail());
        if(userRepo.existsByTel(dto.getTel())) throw new AppException("N° téléphone déjà attribué " + dto.getTel());
        if(userRepo.existsByMatricule(dto.getMatricule())) throw new AppException("Matricule déjà attribué " + dto.getMatricule());
        if(dto.getAssoId() == null) throw new AppException("Veuillez sélectionner l'association");

        // Create user
        AppUser user = new AppUser();
        user.setEmail(dto.getEmail());
        user.setMatricule(dto.getMatricule());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setTel(dto.getTel());
        userRepo.persist(user);

        // Create adhesion
        Adhesion adhesion = new Adhesion(null, new Association(dto.getAssoId()), 
                dto.getSectionId() == null ? null : new Section(dto.getSectionId()), 
                true, user.getUserId().toString());
        adhesionRepo.persist(adhesion);

        return adhesion;
    }

    @Override
    @Transactional
    public Adhesion updateMembre(AdhesionDTO dto)
    {
        Adhesion adhesion = adhesionRepo.findByIdOptional(dto.getAdhesionId())
                .orElseThrow(() -> new AppException("Membre introuvable " + dto.getAdhesionId()));

        AppUser user = userRepo.findByIdOptional(Long.valueOf(dto.getUserId()))
                .orElseThrow(() -> new AppException("Utilisateur introuvable " + dto.getUserId()));

        user.setLastName(dto.getLastName());
        user.setFirstName(dto.getFirstName());
        user.setMatricule(dto.getMatricule());
        user.setTel(dto.getTel());

        adhesion.setSection(new Section(dto.getSectionId()));
        adhesionRepo.persist(adhesion);

        return adhesion;
    }

    @Override
    @Transactional
    public void seDesabonner(Long adhesionId)
    {
        Adhesion adhesion = adhesionRepo.findByIdOptional(adhesionId)
                .orElseThrow(() -> new AppException("Adhésion introuvable"));
        adhesion.setActive(false);
        adhesionRepo.persist(adhesion);
    }

    @Override
    public Page<AdhesionDTO> searchAdhsions(String key, Long assoId, Long sectionId, PageRequest pageable)
    {
        key = StringUtils.stripAccentsToUpperCase(key);
        List<String> userIds = userRepo.find("UPPER(firstName) LIKE ?1 OR UPPER(lastName) LIKE ?1 OR UPPER(email) LIKE ?1", 
                "%" + key + "%").stream().map(u -> u.getUserId().toString()).collect(Collectors.toList());
        return adhesionRepo.searchAdhesions(key, userIds, assoId, sectionId, pageable);
    }

    @Override
    public AdhesionDTO getMembreDTO(String uniqueIdentifier)
    {
        uniqueIdentifier = Optional.ofNullable(uniqueIdentifier).orElse("{#}");
        AppUser user = userRepo.findByUsername(uniqueIdentifier);
        if (user == null) {
            return null;
        }
        return adhesionMapper.mapToAdhesionDto(user);
    }

    @Override
    public List<SelectOption> getOptions(Long assoId)
    {
        if(assoId == null) return Collections.emptyList();
        List<Adhesion> adhesions = adhesionRepo.getAdhesionsByAssoId(assoId);
        if(adhesions == null || adhesions.isEmpty()) return Collections.emptyList();

        List<SelectOption> selectOptions = new ArrayList<>();
        for (Adhesion adhesion : adhesions) {
            AppUser user = userRepo.findByIdOptional(Long.valueOf(adhesion.getUserId())).orElse(null);
            if (user != null) {
                selectOptions.add(new SelectOption(adhesion.getAdhesionId(), user.getFirstName() + " " + user.getLastName()));
            }
        }
        return selectOptions;
    }

    @Override
    public Optional<Adhesion> findByEmailAndSection(String email, Long sectionId)
    {
        AppUser user = userRepo.findByUsername(email);
        if (user == null) {
            return Optional.empty();
        }
        return adhesionRepo.findByUserIdAndSectionId(user.getUserId().toString(), sectionId);
    }

    @Override
    public Optional<Adhesion> findByEmailAndAsso(String email, Long assoId)
    {
        AppUser user = userRepo.findByUsername(email);
        if (user == null) {
            return Optional.empty();
        }
        return adhesionRepo.findByUserIdAndAsso(user.getUserId().toString(), assoId);
    }
}
