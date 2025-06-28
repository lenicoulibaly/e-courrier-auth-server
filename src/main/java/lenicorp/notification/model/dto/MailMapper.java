package lenicorp.notification.model.dto;

import jakarta.enterprise.inject.spi.CDI;
import lenicorp.notification.controller.services.MailConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import lenicorp.notification.model.entities.EmailNotification;
import lenicorp.security.audit.AuditService;
import org.mapstruct.Named;

@Mapper(componentModel = "cdi")
public interface MailMapper
{
    @Mapping(target = "mailId", ignore = true)
    @Mapping(target = "email", source = "to")
    @Mapping(target = "token", ignore = true)
    @Mapping(target = "seen", expression = "java(false)")
    @Mapping(target = "sent", expression = "java(false)")
    @Mapping(target = "mailObject", source = "subject")
    @Mapping(target = "mailMessage", ignore = true)
    @Mapping(target = "senderUsername", expression = "java(mapSenderUsername())")
    @Mapping(target = "systemMailSender", expression = "java(mapSystemMailSender())")
    EmailNotification mapToToEmailNotification(MailRequest request);

    @Named("mapSenderUsername")
    default String mapSenderUsername()
    {
        return CDI.current().select(AuditService.class).get().getCurrentUsername();
    }

    @Named("mapSystemMailSender")
    default String mapSystemMailSender()
    {
        return CDI.current().select(MailConfig.class).get().getUsername();
    }
}
