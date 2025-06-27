package lenicorp.security.audit;

import jakarta.enterprise.inject.spi.CDI;
import org.hibernate.envers.RevisionListener;

public class CustomRevisionListener implements RevisionListener
{

    @Override
    public void newRevision(Object revisionEntity)
    {
        if (revisionEntity instanceof CustomRevisionEntity)
        {
            CustomRevisionEntity revision = (CustomRevisionEntity) revisionEntity;

            try
            {
                var auditService = CDI.current().select(AuditService.class);
                if (!auditService.isUnsatisfied())
                {
                    AuditService service = auditService.get();

                    // Remplir TOUS les champs
                    revision.setUsername(service.getCurrentUsername());
                    revision.setIpAddress(service.getCurrentIpAddress());
                } else
                {
                    // Valeurs par défaut
                    setDefaultValues(revision);
                }
            } catch (Exception e)
            {
                // Valeurs par défaut en cas d'erreur
                setDefaultValues(revision);
            }
        }
    }

    private void setDefaultValues(CustomRevisionEntity revision)
    {
        revision.setUsername("SYSTEM");
        revision.setIpAddress("LOCALHOST");
    }
}