package lenicorp.security.controller.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import lenicorp.security.model.entities.AppUser;

@ApplicationScoped
public class UserRepo implements PanacheRepository<AppUser>
{
    public AppUser findByUsername(String username)
    {
        return find("username", username).firstResult();
    }
}
