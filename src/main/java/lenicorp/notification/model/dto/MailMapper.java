package lenicorp.notification.model.dto;

import jakarta.inject.Inject;
import lenicorp.notification.controller.services.MailConfig;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import lenicorp.notification.model.entities.EmailNotification;
import lenicorp.security.audit.AuditService;

@Mapper(componentModel = "cdi", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class MailMapper {

    @Inject AuditService audit;
    @Inject MailConfig  mailCfg;

    @Mapping(target = "mailId", ignore = true)
    @Mapping(target = "email",      source = "to")
    @Mapping(target = "seen",       constant = "false")
    @Mapping(target = "sent",       constant = "false")
    @Mapping(target = "mailObject", source = "subject")
    @Mapping(target = "senderUsername", expression = "java(audit.getCurrentUsername())")
    @Mapping(target = "systemMailSender", expression = "java(mailCfg.getUsername())")
    public abstract EmailNotification mapToToEmailNotification(MailRequest request);
}
