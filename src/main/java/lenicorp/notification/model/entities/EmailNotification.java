package lenicorp.notification.model.entities;

import jakarta.persistence.*;
import lenicorp.security.model.entities.AppUser;
import lombok.*;
import org.hibernate.envers.Audited;

import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Audited
public class EmailNotification
{
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NOTIF_ID_GEN")
    @SequenceGenerator(name = "NOTIF_ID_GEN", sequenceName = "NOTIF_ID_GEN", allocationSize = 10)
    private Long mailId;
    private String recipientName;
    private String email; //Recipient Email
    private String token;
    private LocalDateTime sendingDate;
    private boolean seen;
    private boolean sent;
    private String mailObject;
    private String mailMessage;

    private String senderUsername;

    private String systemMailSender; /* L'adresse mail utilisée pour envoyer le mail*/

    public EmailNotification(AppUser user, String mailObject, String token, String senderUsername)
    {
        this.recipientName = user.getEmail();
        this.email = user.getEmail();
        this.sendingDate = LocalDateTime.now();
        this.token = token;
        this.mailObject = mailObject;
        this.senderUsername = senderUsername;
    }

    @Override
    public String toString() {
        return mailId +"_"+ recipientName + "_" + email + "_" + token;
    }
}
