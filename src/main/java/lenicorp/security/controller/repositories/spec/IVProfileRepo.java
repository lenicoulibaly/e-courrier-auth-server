package lenicorp.security.controller.repositories.spec;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import lenicorp.security.model.views.VProfile;

/**
 * Repository interface for VProfile entities
 */
public interface IVProfileRepo extends PanacheRepositoryBase<VProfile, String>
{
    // The base PanacheRepositoryBase already provides methods like findAll(), findById(), etc.
}