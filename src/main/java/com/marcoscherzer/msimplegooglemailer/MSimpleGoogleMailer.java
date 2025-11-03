

/**
 * @author Marco Scherzer, Author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
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
import com.google.api.client.util.store.FileDataStoreFactory;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.ArrayList;

import org.apache.commons.codec.binary.Base64;

/**
 * @author Marco Scherzer, Author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public final class MSimpleGoogleMailer {

    private final File credentialsFile;
    private final File tokensDir = new File("tokens");
    private final List<String> scopes = Collections.singletonList(GmailScopes.GMAIL_SEND);

    private final String userEmail;
    private final Gmail service;

    /**
     * @author Marco Scherzer, Author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MSimpleGoogleMailer(String applicationName, String userEmail, File credentialsFile) throws Exception {
        MUtil.checkMailAddress(userEmail);
        this.userEmail = userEmail;
        if (credentialsFile == null || !credentialsFile.exists()) throw new IllegalArgumentException("Credential file not found: " + credentialsFile);
        this.credentialsFile = credentialsFile;
        if (applicationName == null || applicationName.isBlank()) throw new IllegalArgumentException("Application name must not be empty.");
        if (!tokensDir.exists()) tokensDir.mkdirs();
        tokensDir.setReadable(true, true);
        tokensDir.setWritable(true, true);


        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
        Credential credential = authorize(httpTransport, jsonFactory);

        this.service = new Gmail.Builder(httpTransport, jsonFactory, credential)
                .setApplicationName(applicationName)
                .build();
    }

    /**
     * @author Marco Scherzer, Author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private Credential authorize(HttpTransport httpTransport, JsonFactory jsonFactory)
            throws IOException {
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(jsonFactory, new FileReader(credentialsFile));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, jsonFactory, clientSecrets, scopes)
                .setDataStoreFactory(new FileDataStoreFactory(tokensDir))
                .setAccessType("offline")
                .build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();

        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    /**
     * @author Marco Scherzer, Author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final void send(MOutgoingMail mail) {
        try {
            Properties props = new Properties();
            Session session = Session.getDefaultInstance(props, null);
            MimeMessage email = new MimeMessage(session);
            email.setFrom(new InternetAddress(this.userEmail));
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
