package lenicorp.security.controller.repositories.impl;

import jakarta.enterprise.context.ApplicationScoped;
import lenicorp.security.controller.repositories.spec.IAuthorityRepo;

@ApplicationScoped
public class AuthorityRepo implements IAuthorityRepo
{
    @Override
    public boolean existsByCode(String code)
    {
        return this.count("code = ?1", code) > 0;
    }

    @Override
    public boolean existsByName(String name)
    {
        return count("name = ?1", name) > 0;
    }

    @Override
    public boolean existsByName(String name, String code)
    {
        return count("name = ?1 and code <> ?2", name, code) > 0;
    }

    @Override
    public boolean existsByCodeAndType(String code, String typeCode)
    {
        return count("code = ?1 and type.code = ?2", code, typeCode) > 0;
    }
}
