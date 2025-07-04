package lenicorp.security.controller.repositories.spec;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import lenicorp.security.model.entities.AppAuthority;

public interface IAuthorityRepo extends PanacheRepositoryBase<AppAuthority, String>
{
    boolean existsByCode(String code);

    boolean existsByName(String name);

    boolean existsByName(String name, String code);

    boolean existsByCodeAndType(String code, String typeCode);
}
