package lenicorp.notification.controller.services;

import lenicorp.notification.model.dto.MailRequest;
import lenicorp.notification.model.dto.MailResponse;

import java.util.concurrent.CompletableFuture;

public interface MailServiceInterface
{
    /**
     * Envoie un email pour activer un compte utilisateur.
     *
     * @param destinataire   L'adresse email du destinataire.
     * @param nomDestinataire Le nom du destinataire.
     * @param lienActivation Le lien pour activer le compte.
     * @return Un CompletableFuture de MailResponse décrivant le résultat de l'envoi.
     */
    CompletableFuture<MailResponse> envoyerEmailActivation(String destinataire, String nomDestinataire, String lienActivation);

    /**
     * Envoie un email pour réinitialiser le mot de passe.
     *
     * @param destinataire   L'adresse email du destinataire.
     * @param nomDestinataire Le nom du destinataire.
     * @param lienReset      Le lien pour réinitialiser le mot de passe.
     * @return Un CompletableFuture de MailResponse décrivant le résultat de l'envoi.
     */
    CompletableFuture<MailResponse> envoyerEmailReinitialisation(String destinataire, String nomDestinataire, String lienReset);

    /**
     * Envoie un email avec le contenu spécifié.
     *
     * @param mailRequest Les informations sur l'email à envoyer.
     * @return Un CompletableFuture de MailResponse décrivant le résultat de l'envoi.
     */
    CompletableFuture<MailResponse> sendMailAsync(MailRequest mailRequest);

}