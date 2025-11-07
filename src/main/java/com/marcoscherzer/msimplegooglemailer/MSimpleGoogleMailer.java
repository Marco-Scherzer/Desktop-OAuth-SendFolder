package com.marcoscherzer.msimplegooglemailer;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.MemoryDataStoreFactory;
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
    public static void setClientKeystoreDir(String clientSecretFileDir) {
        clientSecretDir = clientSecretFileDir;
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MSimpleGoogleMailer(String applicationName, String keystorePassword, boolean forceOAuth) throws Exception {
        File keystoreFile = new File(clientSecretDir, "mystore.p12");
        File jsonFile = new File(clientSecretDir, "client_secret.json");

            if (!keystoreFile.exists()) checkParameters(applicationName, keystorePassword);
            try {
                this.keystore = new MSimpleKeystore(keystoreFile, keystorePassword);
  //generelles problem für !created wenn jsonfile anfangas vergessen wurde und der nächste aufrf erfolgt
                boolean newCreated= keystore.loadKeyStoreOrCreateKeyStoreIfNotExists();
            if (!newCreated && !keystore.isCompletelyInitialized("clientId","google-client-id", "google-client-secret")) {
                throw new Exception("KeyStore not properly initalized.");
            }
            HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
            Credential credential;
            String clientId;
            String clientSecret;
//generelles problem für !created wenn jsonfile anfangas vergessen wurde und der nächste aufrf erfolgt
            if (newCreated) {
                if (jsonFile.exists()) {
                    GoogleClientSecrets secrets = GoogleClientSecrets.load(jsonFactory, new FileReader(jsonFile));
                    clientId = secrets.getDetails().getClientId();
                    clientSecret = secrets.getDetails().getClientSecret();
                    if (clientId != null && clientSecret != null) {
                        keystore.add("google-client-id", clientId).add("google-client-secret", clientSecret);
                    } else {
                        throw new IllegalStateException("client_secret.json does not contain valid credentials.");
                    }
                } else {
                    throw new IllegalStateException("client_secret.json must be placed in the directory \"" + clientSecretDir + "\" before first launch.");
                }

                UUID uuid = UUID.randomUUID();
                System.out.println("Client security UUID generated: " + uuid);
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
            GoogleAuthorizationCodeFlow flow;

            if (forceOAuth) {
                if (keystore.contains("OAuth")) {
                    System.out.println("Removing persistent OAuth token.");
                    keystore.remove("OAuth");
                }
                flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, jsonFactory, clientSecrets, scopes)
                        .setAccessType("online")
                        .setApprovalPrompt("force")
                        .setCredentialDataStore(new MemoryDataStoreFactory().getDataStore("tempsession"))
                        .build();
            } else {
                flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, jsonFactory, clientSecrets, scopes)
                        .setAccessType("offline")
                        .setCredentialDataStore(new MSimpleKeystoreDataStoreFactory(keystore).getDataStore("OAuth"))
                        .build();
            }

            LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
            credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("OAuth");
            if (credential == null) {
                throw new IllegalStateException("No stored OAuth credential found.");
            }

            applicationName += " [" + keystore.get("clientId") + "]";

            this.service = new Gmail.Builder(httpTransport, jsonFactory, credential)
                    .setApplicationName(applicationName)
                    .build();

        } catch (Exception exc) {
                System.out.println("Error in initialization");
                try {
                if (keystore != null) {
                    System.out.println("resetting KeyStore");
                    keystore.clear();
                }
                else{
                    throw new Exception("Initialization could not be completed (no KeyStore File written yet).", exc);
                }
            } catch (Exception exc2) {
                throw new Exception("Warning: Initialization could not be completed and Keystore could not be reset. Please delete it manually.", exc);
            }
        }

        if (jsonFile.exists()) {
            boolean jsonFileDeleted = jsonFile.delete();
            if (!jsonFileDeleted) {
                System.out.println("Warning: File \"client_secret.json\" could not be deleted. Please delete it manually.");
            } else {
                System.out.println("client_secret.json successfully imported and deleted.");
            }
        }
    }
    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private void checkParameters(String applicationName, String keystorePassword) throws IllegalArgumentException {
        if (applicationName == null || applicationName.isBlank()) {
            throw new IllegalArgumentException("Application name must not be empty.");
        }
        checkPasswordComplexity(keystorePassword, 15, true, true, true);
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
    public void send(MOutgoingMail mail) throws Exception {
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
            System.out.println("Mail successfully sent. ID: " + sentMessage.getId());

        } catch (Exception exc) {
            throw new Exception("Error while sending the email.", exc);
        }
    }
}



