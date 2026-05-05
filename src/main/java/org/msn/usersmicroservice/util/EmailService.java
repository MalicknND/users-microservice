package org.msn.usersmicroservice.util;

// Import pour gérer les erreurs liées aux emails (ex: mauvais format, problème SMTP)
import jakarta.mail.MessagingException;

// Représente un email complet (objet MIME : contenu + headers)
import jakarta.mail.internet.MimeMessage;

// Lombok : génère automatiquement un constructeur avec tous les champs (ici mailSender)
import lombok.AllArgsConstructor;

// Interface Spring pour envoyer des emails
import org.springframework.mail.javamail.JavaMailSender;

// Helper pour simplifier la création d’un email (HTML, pièces jointes, etc.)
import org.springframework.mail.javamail.MimeMessageHelper;

// Indique que cette classe est un service Spring (injectable ailleurs)
import org.springframework.stereotype.Service;

// Lombok : crée un constructeur avec injection du JavaMailSender
@AllArgsConstructor

// Déclare ce composant comme un service Spring (géré par le conteneur)
@Service
public class EmailService implements EmailSender {

    // Dépendance pour envoyer des emails via SMTP (configurée dans application.properties)
    private final JavaMailSender mailSender;

    // Méthode définie dans l'interface EmailSender
    @Override
    public void sendEmail(String to, String email) {
        try {
            // Création d’un email vide (structure MIME)
            MimeMessage mimeMessage = mailSender.createMimeMessage();

            // Helper pour manipuler facilement le message
            // "utf-8" → encodage pour supporter accents / caractères spéciaux
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            // Contenu du mail
            // true → indique que c’est du HTML (sinon texte brut)
            helper.setText(email, true);

            // Adresse du destinataire
            helper.setTo(to);

            // Sujet de l’email
            helper.setSubject("Confirm your email");

            // Adresse de l’expéditeur
            helper.setFrom("msndiayedev@gmail.com");

            // Envoi effectif de l’email via SMTP
            mailSender.send(mimeMessage);

        } catch (MessagingException e) {
            // Si erreur (format, SMTP, etc.), on lève une exception runtime
            // ⚠️ améliorable : log + message plus précis
            throw new IllegalStateException("failed to send email");
        }
    }
}