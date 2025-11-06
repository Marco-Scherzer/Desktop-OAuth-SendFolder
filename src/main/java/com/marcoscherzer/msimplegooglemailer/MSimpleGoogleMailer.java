package com.marcoscherzer.msimplegooglemailer;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Message;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.Multipart;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import org.apache.commons.codec.binary.Base64;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import static com.marcoscherzer.msimplegooglemailer.MUtil.checkPasswordComplexity;

/**
 * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public final class MSimpleGoogleMailer {

    private static String clientSecretDir = System.getProperty("user.dir");
    private final List<String> scopes = Collections.singletonList(GmailScopes.GMAIL_SEND);
    private Gmail service;
    private MSimpleKeystore keystore;

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public static final void setClientKeystoreDir(String clientSecretFileDir) {
        clientSecretDir = clientSecretFileDir;
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MSimpleGoogleMailer(String applicationName, String keystorePassword, boolean forceOAuth) throws Exception {
        File keystoreFile = new File(clientSecretDir, "mystore.p12");
        File jsonFile = new File(clientSecretDir, "client_secret.json");

        try {
            if (!keystoreFile.exists()) checkParameters(applicationName, keystorePassword);

            this.keystore = new MSimpleKeystore(keystoreFile, keystorePassword);
            boolean newCreated = keystore.loadKeyStoreOrCreateKeyStoreIfNotExists();
            if (!newCreated && !keystore.isCompletelyInitialized("clientId", "google-client-id", "google-client-secret")) {
                keystore.clear();
            }

            HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
            Credential credential;
            String clientId;
            String clientSecret;

            if (newCreated) {
                if (jsonFile.exists()) {
                    GoogleClientSecrets secrets = GoogleClientSecrets.load(jsonFactory, new FileReader(jsonFile));
                    clientId = secrets.getDetails().getClientId();
                    clientSecret = secrets.getDetails().getClientSecret();
                    if (clientId != null && clientSecret != null) {
                        keystore.add("google-client-id", clientId).add("google-client-secret", clientSecret);
                    } else {
                        throw new IllegalStateException("client_secret.json enthält keine gültigen Daten.");
                    }
                } else {
                    throw new IllegalStateException("client_secret.json muss vor dem ersten Start im Verzeichnis \"" + clientSecretDir + "\" abgelegt werden.");
                }

                UUID uuid = UUID.randomUUID();
                System.out.println("Client Sicherheits-UUID wurde erstellt: " + uuid);
                keystore.add("clientId", uuid.toString());
            }

            clientId = keystore.get("google-client-id");
            clientSecret = keystore.get("google-client-secret");

            GoogleClientSecrets.Details details = new GoogleClientSecrets.Details()
                    .setClientId(clientId)
                    .setClientSecret(clientSecret)
                    .setAuthUri("https://accounts.google.com/o/oauth2/auth")
                    .setTokenUri("https://oauth2.googleapis.com/token");

            GoogleClientSecrets clientSecrets = new GoogleClientSecrets().setInstalled(details);

            if (newCreated || forceOAuth) {
                GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, jsonFactory, clientSecrets, scopes)
                        .setAccessType("online") // keine Refresh-Tokens
                        .setCredentialDataStore(new com.google.api.client.util.store.MemoryDataStoreFactory().getDataStore("temp-session"))
                        .build();

                LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
                credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("session-" + UUID.randomUUID());

            } else {
                GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, jsonFactory, clientSecrets, scopes)
                        .setAccessType("offline")
                        .setCredentialDataStore(MSimpleKeystoreDataStore.getDataStoreFactory(keystore).getDataStore(keystore.get("clientId")))
                        .build();

                credential = flow.loadCredential(keystore.get("clientId"));
                if (credential == null) {
                    throw new IllegalStateException("Kein gespeichertes OAuth-Credential gefunden. Bitte forceOAuth aktivieren.");
                }
            }

            applicationName += " [" + keystore.get("clientId") + "]";

            this.service = new Gmail.Builder(httpTransport, jsonFactory, credential)
                    .setApplicationName(applicationName)
                    .build();

        } catch (MPasswordIncorrectException exc) {
            throw exc;
        } catch (Exception exc) {
            String msg = "Initialisierung konnte nicht abgeschlossen werden. ";
            try {
                if (keystore != null) {
                    keystore.clear();
                    msg += " Keystore wurde erfolgreich zurückgesetzt.";
                }
            } catch (Exception exc2) {
                msg += " und Keystore konnte nicht zurückgesetzt werden. Bitte manuell löschen.";
                throw new RuntimeException(msg, exc);
            }
            throw new RuntimeException(msg, exc);
        }

        if (jsonFile.exists()) {
            boolean jsonFileDeleted = jsonFile.delete();
            if (!jsonFileDeleted) {
                System.out.println("Warnung: Datei \"client_secret.json\" konnte nicht gelöscht werden. Bitte manuell löschen.");
            } else {
                System.out.println("client_secret.json erfolgreich importiert und gelöscht.");
            }
        }
    }


    /**
         * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         */
        private final void checkParameters(String applicationName, String keystorePassword) throws IllegalArgumentException{
            if (applicationName == null || applicationName.isBlank()) throw new IllegalArgumentException("Application name must not be empty.");
            checkPasswordComplexity(keystorePassword, 8, true, true, true);
        }

        /**
         * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         */
    public MSimpleKeystore getKeystore() {
        return this.keystore;
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final void send(MOutgoingMail mail) throws Exception {
        try {
            Properties props = new Properties();
            Session session = Session.getDefaultInstance(props, null);
            MimeMessage email = new MimeMessage(session);
            email.setFrom(new InternetAddress(mail.getFrom()));
            email.addRecipient(jakarta.mail.Message.RecipientType.TO, new InternetAddress(mail.getTo()));
            email.setSubject(mail.getSubject());

            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(mail.getMessageText());

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(textPart);

            for (String filePath : mail.getAttachments()) {
                File file = new File(filePath);
                MimeBodyPart attachmentPart = new MimeBodyPart();
                DataSource source = new FileDataSource(file);
                attachmentPart.setDataHandler(new DataHandler(source));
                attachmentPart.setFileName(file.getName());
                multipart.addBodyPart(attachmentPart);
            }

            email.setContent(multipart);

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            email.writeTo(buffer);
            byte[] rawMessageBytes = buffer.toByteArray();
            String encodedEmail = Base64.encodeBase64URLSafeString(rawMessageBytes);

            Message message = new Message();
            message.setRaw(encodedEmail);

            Message sentMessage = service.users().messages().send("me", message).execute();
            System.out.println("Mail erfolgreich gesendet. ID: " + sentMessage.getId());

        } catch (Exception exc) {
            throw new RuntimeException("Fehler beim Mail versenden der Mail.",exc);
        }
    }
}


