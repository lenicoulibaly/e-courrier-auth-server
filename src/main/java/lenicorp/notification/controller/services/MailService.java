package lenicorp.notification.controller.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;
import jakarta.transaction.SystemException;
import jakarta.transaction.Transactional;
import jakarta.transaction.UserTransaction;
import lenicorp.notification.controller.dao.EmailNotificationRepo;
import lenicorp.notification.model.dto.MailMapper;
import lenicorp.notification.model.dto.MailRequest;
import lenicorp.notification.model.dto.MailResponse;
import lenicorp.notification.model.entities.EmailNotification;
import lombok.extern.slf4j.Slf4j;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;


import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

@ApplicationScoped
@Slf4j
public class MailService implements MailServiceInterface
{
    @Inject Mailer mailer;
    @Inject MailConfig mailConfig;
    @Inject EmailNotificationRepo emailNotificationRepo;
    @Inject MailMapper mailMapper;
    @Inject UserTransaction userTransaction;

    @Override
    public CompletableFuture<MailResponse> envoyerEmailActivation(String destinataire, String nomDestinataire, String lienActivation)
    {
        String sujet = "Activation de votre compte e-courrier";

        // Charger le fichier HTML d'activation
        String corpsHtml = chargerTemplateHtml("templates/emails/activation-email.html")
                .replace("${nomDestinataire}", nomDestinataire)
                .replace("${lienActivation}", lienActivation);

        MailRequest mailRequest = MailRequest.builder()
                .to(destinataire)
                .subject(sujet)
                .content(corpsHtml)
                .isHtml(true)
                .build();

        return sendMailAsync(mailRequest);
    }

    @Override
    public CompletableFuture<MailResponse> envoyerEmailReinitialisation(String destinataire, String nomDestinataire, String lienReset)
    {
        String sujet = "Réinitialisation de votre mot de passe";

        // Charger le fichier HTML de réinitialisation
        String corpsHtml = chargerTemplateHtml("templates/emails/password-reset-email.html")
                .replace("${nomDestinataire}", nomDestinataire)
                .replace("${lienReset}", lienReset);

        MailRequest mailRequest = MailRequest.builder()
                .to(destinataire)
                .subject(sujet)
                .content(corpsHtml)
                .isHtml(true)
                .build();

        return sendMailAsync(mailRequest);
    }

    @Override
    @ActivateRequestContext
    public CompletableFuture<MailResponse> sendMailAsync(MailRequest mailRequest) {
        EmailNotification notification = mailMapper.mapToToEmailNotification(mailRequest);
        return CompletableFuture.supplyAsync(() -> {
            try {
                userTransaction.begin();
                Mail mail = createMail(mailRequest);

                // Tenter l'envoi
                mailer.send(mail);

                // Si succès, marquer comme envoyé et persister
                notification.setSent(true);
                notification.setSendingDate(LocalDateTime.now());
                emailNotificationRepo.persist(notification);

                userTransaction.commit();

                return MailResponse.builder()
                        .success(true)
                        .sentAt(LocalDateTime.now())
                        .build();

            } catch (Exception e) {
                try {
                    userTransaction.rollback();
                } catch (SystemException rollbackException) {
                    log.error("Erreur lors du rollback", rollbackException);
                }

                log.error("Erreur lors de l'envoi de l'email à: {}", mailRequest.getTo(), e);
                return MailResponse.builder()
                        .success(false)
                        .errorMessage(e.getMessage())
                        .sentAt(LocalDateTime.now())
                        .build();
            }
        });
    }


    private Mail createMail(MailRequest mailRequest)
    {
        Mail mail = Mail.withHtml(mailRequest.getTo(), mailRequest.getSubject(), mailRequest.getContent());
        if (mailRequest.getCc() != null && !mailRequest.getCc().isEmpty())
        {
            mail.addCc(mailRequest.getCc().toArray(new String[0]));
        }
        if (mailRequest.getBcc() != null && !mailRequest.getBcc().isEmpty())
        {
            mail.addBcc(mailRequest.getBcc().toArray(new String[0]));
        }
        if (mailRequest.getHeaders() != null && !mailRequest.getHeaders().isEmpty())
        {
            mailRequest.getHeaders().forEach(mail::addHeader);
        }
        return mail;
    }

    private String chargerTemplateHtml(String cheminTemplate)
    {
        try
        {
            Path chemin = Paths.get(getClass().getClassLoader().getResource(cheminTemplate).toURI());
            return Files.readString(chemin);
        } catch (IOException | NullPointerException | URISyntaxException e)
        {
            log.error("Erreur lors du chargement du template HTML : {}", cheminTemplate, e);
            throw new RuntimeException("Impossible de charger le template : " + cheminTemplate, e);
        }
    }

}