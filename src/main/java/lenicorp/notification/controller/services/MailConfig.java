package lenicorp.notification.controller.services;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.Getter;
import java.time.Duration;

@ApplicationScoped
@Getter
public class MailConfig
{
    @ConfigProperty(name = "quarkus.mailer.host")
    String smtpHost;
    
    @ConfigProperty(name = "quarkus.mailer.port")
    Integer smtpPort;
    
    @ConfigProperty(name = "quarkus.mailer.username")
    String username;
    
    @ConfigProperty(name = "quarkus.mailer.from")
    String defaultFrom;
    
    @ConfigProperty(name = "quarkus.mailer.timeout")
    Duration timeout;
    
    @ConfigProperty(name = "quarkus.mailer.connection-timeout")
    Duration connectionTimeout;
    
    @ConfigProperty(name = "quarkus.mailer.mock", defaultValue = "false")
    Boolean mockMode;
    
    @ConfigProperty(name = "quarkus.mailer.ssl", defaultValue = "false")
    Boolean sslEnabled;
    
    @ConfigProperty(name = "quarkus.mailer.start-tls")
    String startTls;
    
    @ConfigProperty(name = "quarkus.mailer.trust-all", defaultValue = "false")
    Boolean trustAll;
    
    @ConfigProperty(name = "quarkus.mailer.bounce-address")
    String bounceAddress;
    
    @ConfigProperty(name = "quarkus.mailer.template-from")
    String templateFrom;
}