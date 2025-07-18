package lenicorp.types.controller.repositories.spec;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import lenicorp.types.model.dtos.TypeDTO;
import lenicorp.types.model.entities.Type;
import lenicorp.utilities.Page;
import lenicorp.utilities.PageRequest;

import java.util.List;

public interface ITypeRepo extends PanacheRepositoryBase<Type, String>
{
    Boolean existsByCode(String code);

    Boolean existsByName(String name);

    Boolean existsByName(String name, String code);

    List<TypeDTO> findDirectSousTypes(String parentCode);

    Page<TypeDTO> searchTypes(String key, List<String> groupCodes, PageRequest pageRequest);

    List<TypeDTO> findByGroupCode(String groupCode);

    List<Type> findByTypeGroup(String groupCode);

    List<String> getParentTypeCodes(String typeCode);

    boolean existsByCodeAndGroupCode(String typeCode, String groupCode);
}