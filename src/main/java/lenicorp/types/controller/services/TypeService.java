
package lenicorp.types.controller.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lenicorp.exceptions.AppException;
import lenicorp.types.controller.repositories.TypeGroupRepo;
import lenicorp.types.controller.repositories.TypeMappingRepo;
import lenicorp.types.controller.repositories.TypeRepo;
import lenicorp.utilities.Page;
import lenicorp.utilities.PageRequest;
import lenicorp.types.model.dtos.TypeDTO;
import lenicorp.types.model.dtos.TypeGroupDTO;
import lenicorp.types.model.entities.Type;
import lenicorp.types.model.entities.TypeGroup;
import lenicorp.types.model.entities.TypeMapping;
import lenicorp.types.model.mappers.TypeGroupMapper;
import lenicorp.types.model.mappers.TypeMapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class TypeService implements ITypeService
{

    @Inject
    private TypeRepo typeRepo;
    @Inject
    private TypeGroupRepo typeGroupRepo;
    @Inject
    private TypeMappingRepo tmRepo;
    @Inject
    private TypeMapper typeMapper;
    @Inject
    private TypeGroupMapper typeGroupMapper;

    @Override
    @Transactional
    public TypeDTO createType(TypeDTO dto)
    {
        // Validation de l'existence du code
        if (typeRepo.existsByCode(dto.getCode()))
        {
            throw new AppException("Un type avec ce code existe déjà : " + dto.getCode());
        }

        // Validation de l'existence du nom
        if (typeRepo.existsByName(dto.getName()))
        {
            throw new AppException("Un type avec ce nom existe déjà : " + dto.getName());
        }

        Type type = typeMapper.mapToType(dto);
        typeRepo.persist(type);
        return typeMapper.mapToDto(type);
    }

    @Override
    @Transactional
    public TypeDTO updateType(TypeDTO dto)
    {
        Type existingType = typeRepo.findById(dto.getCode());
        if (existingType == null) throw new AppException("Type non trouvé avec le code : " + dto.getCode());
        if (typeRepo.existsByName(dto.getName(), dto.getCode()))
            throw new AppException("Un autre type avec ce nom existe déjà : " + dto.getName());
        existingType = typeMapper.mapToType(dto, existingType);
        if (dto.getGroupCode() != null) existingType.typeGroup = new TypeGroup(dto.getGroupCode());
        typeRepo.persist(existingType);
        return typeMapper.mapToDto(existingType);
    }

    @Override
    public Page<TypeDTO> searchTypes(String key, List<String> groupCodes, PageRequest pageRequest)
    {
        return typeRepo.searchTypes(key, groupCodes, pageRequest);
    }

    @Override
    @Transactional
    public TypeGroupDTO createTypeGroup(TypeGroupDTO dto)
    {
        // Validation de l'existence du code
        if (typeGroupRepo.existsByGroupCode(dto.getGroupCode()))
        {
            throw new AppException("Un groupe avec ce code existe déjà : " + dto.getGroupCode());
        }

        // Validation de l'existence du nom
        if (typeGroupRepo.existsByName(dto.getName()))
        {
            throw new AppException("Un groupe avec ce nom existe déjà : " + dto.getName());
        }

        TypeGroup typeGroup = typeGroupMapper.mapToEntity(dto);
        typeGroupRepo.persist(typeGroup);
        return typeGroupMapper.mapToDto(typeGroup);
    }

    @Override
    @Transactional
    public TypeGroupDTO updateTypeGroup(TypeGroupDTO dto)
    {
        TypeGroup existingGroup = typeGroupRepo.findById(dto.getGroupCode());
        if (existingGroup == null)
        {
            throw new AppException("Groupe non trouvé avec le code : " + dto.getGroupCode());
        }

        // Validation du nom (pas le même nom pour un autre code)
        if (typeGroupRepo.existsByName(dto.getName(), dto.getGroupCode()))
        {
            throw new AppException("Un autre groupe avec ce nom existe déjà : " + dto.getName());
        }

        existingGroup.name = dto.getName();
        typeGroupRepo.persist(existingGroup);
        return typeGroupMapper.mapToDto(existingGroup);
    }

    @Override
    public Page<TypeGroupDTO> searchTypeGroups(String key, PageRequest pageRequest)
    {
        return typeGroupRepo.searchTypeGroups(key, pageRequest);
    }

    @Override
    public List<TypeDTO> getDirectSousTypes(String parentCode)
    {
        return typeRepo.findDirectSousTypes(parentCode);
    }

    @Override
    public boolean parentHasDirectSousType(String parentCode, String childCode)
    {
        return tmRepo.parentHasDirectSousType(parentCode, childCode);
    }

    @Override
    public boolean parentHasDistantSousType(String parentCode, String childCode)
    {
        if(parentHasDirectSousType(parentCode, childCode)) return true;
        if(!typeRepo.existsByCode(parentCode) || !typeRepo.existsByCode(childCode)) return false;
        return typeRepo.findDirectSousTypes(parentCode).stream().anyMatch(st->parentHasDistantSousType(st.getCode(), childCode));
    }

    @Override
    @Transactional
    public void setSousTypes(TypeDTO dto)
    {
        Type type = typeRepo.findByIdOptional(dto.getCode()).orElseThrow(()->new AppException("Code de type introuvable : " + dto.getCode()));
        List<String> inputSousTypeCodes = dto.getSousTypeCodes() == null || dto.getSousTypeCodes().isEmpty() ? Collections.emptyList() : dto.getSousTypeCodes();

        List<String> sousTypeCodesToRemove = tmRepo.findSousTypeCodesToRemove(dto.getCode(), inputSousTypeCodes);
        List<String> sousTypeCodesToAdd = tmRepo.findSousTypeCodesToAdd(dto.getCode(), inputSousTypeCodes);
        if(sousTypeCodesToRemove != null && !sousTypeCodesToRemove.isEmpty()) tmRepo.removeSousTypes(dto.getCode(), sousTypeCodesToRemove);

        if(sousTypeCodesToAdd != null && !sousTypeCodesToAdd.isEmpty())
        {
            sousTypeCodesToAdd = sousTypeCodesToAdd.stream().filter(sta->!this.parentHasDistantSousType(sta, dto.getCode())).collect(Collectors.toList());
            sousTypeCodesToAdd.forEach(st->
            {
                tmRepo.persist(new TypeMapping(null, new Type(dto.getCode()), new Type(st)));
            });
        }
    }

    @Override
    public List<TypeDTO> getPossibleSousTypes(String parentCode)
    {
        return typeRepo.findByTypeGroup(typeGroupRepo.findGroupCodeByTypeCode(parentCode)).stream()
                .filter(t->!this.parentHasDistantSousType(t.code, parentCode) && !t.code.equals(parentCode))
                .map(typeMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TypeDTO> getTypesByGroupCode(String groupCode)
    {
        return typeRepo.findByGroupCode(groupCode);
    }

    @Override
    public List<TypeGroupDTO> getAllTypeGroups()
    {
        return typeGroupRepo.getAllTypeGroupes();
    }
}