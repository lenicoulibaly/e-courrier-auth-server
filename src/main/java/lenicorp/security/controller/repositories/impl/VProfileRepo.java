package lenicorp.security.controller.repositories.impl;

import jakarta.enterprise.context.ApplicationScoped;
import lenicorp.security.controller.repositories.spec.IVProfileRepo;

@ApplicationScoped
public class VProfileRepo implements IVProfileRepo
{
    // The implementation inherits all methods from PanacheRepositoryBase
    // No need to override any methods as we're using the default implementations
}