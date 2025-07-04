package lenicorp.security.controller.repositories.spec;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import lenicorp.security.model.entities.AuthToken;

public interface IAuthTokenRepo extends PanacheRepository<AuthToken>
{
    AuthToken findByToken(String token);

    boolean existsByTokenAndUserId(String token, Long userId);

    boolean tokenHasNotExpired(String token);

    boolean tokenIsAlreadyUsed(String token);
}