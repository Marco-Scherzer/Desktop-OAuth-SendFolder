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
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public final class MSimpleGoogleMailer {

    private static String clientSecretDir = System.getProperty("user.dir");
    private final List<String> scopes = Collections.singletonList(GmailScopes.GMAIL_SEND);
    private Gmail service;
    private MSimpleKeyStore keystore;

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public static void setInitialClientSecretFileReadDir(String clientSecretFileDir) {
        clientSecretDir = clientSecretFileDir;
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MSimpleGoogleMailer(String applicationName, String keystorePassword, boolean forceOAuth) throws Exception {
        if (applicationName == null || applicationName.isBlank()) {
            throw new IllegalArgumentException("Application name must not be empty.");
        }

        File keystoreFile = new File(clientSecretDir, "mystore.jks");
        if (!keystoreFile.exists()) {
            System.out.println("Keystore wird neu erstellt: " + keystoreFile.getAbsolutePath());
        }

        this.keystore = new MSimpleKeyStore(keystoreFile.getAbsolutePath(), keystorePassword);
        checkStoreForExistingClientTokenOrReadItFromDirectoryAndDeleteFile(keystore);

        String clientUuid = keystore.getToken("clientId");
        if (clientUuid != null && !clientUuid.isBlank()) {
            applicationName += " [" + clientUuid + "]";
        }

        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

        String clientId = keystore.getToken("google-client-id");
        String clientSecret = keystore.getToken("google-client-secret");

        String accessToken = keystore.getToken("google-access-token");
        String refreshToken = keystore.getToken("google-refresh-token");

        Credential credential;

        if (!forceOAuth && accessToken != null && refreshToken != null) {
            credential = new Credential.Builder(BearerToken.authorizationHeaderAccessMethod())
                    .setTransport(httpTransport)
                    .setJsonFactory(jsonFactory)
                    .setClientAuthentication(new ClientParametersAuthentication(clientId, clientSecret))
                    .setTokenServerEncodedUrl("https://oauth2.googleapis.com/token")
                    .build();
            credential.setAccessToken(accessToken);
            credential.setRefreshToken(refreshToken);
        } else {
            credential = authenticate(applicationName, httpTransport, jsonFactory, keystore, clientId, clientSecret);
            accessToken = credential.getAccessToken();
            refreshToken = credential.getRefreshToken();
            if (accessToken != null) keystore.addToken("google-access-token", accessToken);
            if (refreshToken != null) keystore.addToken("google-refresh-token", refreshToken);
        }

        this.service = new Gmail.Builder(httpTransport, jsonFactory, credential)
                .setApplicationName(applicationName)
                .build();
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MSimpleKeyStore getKeystore() {
        return this.keystore;
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private static void checkStoreForExistingClientTokenOrReadItFromDirectoryAndDeleteFile(MSimpleKeyStore store) throws Exception {
        File jsonFile = new File(clientSecretDir, "client_secret.json");

        if (jsonFile.exists()) {
            JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
            GoogleClientSecrets secrets = GoogleClientSecrets.load(jsonFactory, new FileReader(jsonFile));

            String clientId = secrets.getDetails().getClientId();
            String clientSecret = secrets.getDetails().getClientSecret();

            if (clientId != null && clientSecret != null) {
                store.addToken("google-client-id", clientId).addToken("google-client-secret", clientSecret);
                if (!jsonFile.delete()) {
                    System.err.println("Warnung: client_secret.json konnte nicht gelöscht werden.");
                } else {
                    System.out.println("client_secret.json erfolgreich importiert und gelöscht.");
                }
            } else {
                throw new IllegalStateException("client_secret.json enthält keine gültigen Daten.");
            }
        } else {
            if (store.getToken("google-client-id") == null || store.getToken("google-client-secret") == null) {
                throw new IllegalStateException("Kein client_secret.json gefunden und keine Client-Daten im Keystore. Bitte client_secret.json in " + clientSecretDir + " ablegen.");
            }
        }
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private Credential authenticate(String applicationName, HttpTransport httpTransport, JsonFactory jsonFactory, MSimpleKeyStore keystore, String clientId, String clientSecret) throws IOException {
        GoogleClientSecrets.Details details = new GoogleClientSecrets.Details();
        details.setClientId(clientId);
        details.setClientSecret(clientSecret);
        details.setAuthUri("https://accounts.google.com/o/oauth2/auth");
        details.setTokenUri("https://oauth2.googleapis.com/token");

        GoogleClientSecrets clientSecrets = new GoogleClientSecrets().setInstalled(details);

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, jsonFactory, clientSecrets, scopes)
                .setAccessType("offline")
                .build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();

        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final void send(MOutgoingMail mail) {
        try {
            Properties props = new Properties();
            Session session = Session.getDefaultInstance(props, null);
            MimeMessage email = new MimeMessage(session);
            email.setFrom(new InternetAddress(mail.from));
            email.addRecipient(jakarta.mail.Message.RecipientType.TO, new InternetAddress(mail.to));
            email.setSubject(mail.subject);

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
            exc.printStackTrace();
        }
    }
}


