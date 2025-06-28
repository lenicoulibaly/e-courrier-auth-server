package lenicorp.notification.controller.dao;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import lenicorp.notification.model.entities.EmailNotification;

@ApplicationScoped
public class EmailNotificationRepo implements PanacheRepository<EmailNotification>
{

}