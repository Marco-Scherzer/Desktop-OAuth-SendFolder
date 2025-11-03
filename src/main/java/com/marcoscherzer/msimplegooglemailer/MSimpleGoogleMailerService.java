package com.marcoscherzer.msimplegooglemailer;

import javax.net.ssl.*;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyStore;

/**
 * @author Marco Scherzer, Author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public class MSimpleGoogleMailerService {

    /**
     * @author Marco Scherzer, Author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public static void main(String[] args) {
        try {
            Path credPath = Paths.get(System.getProperty("user.dir"), "client_secret.json");
            String appName = "theRegisteredGoogleServiceConsoleAppName";

            MSimpleGoogleMailer mailer = new MSimpleGoogleMailer(
                    appName,
                    "anEmailAddressValidForMyGoogleMailAccount",
                    credPath.toFile()
            );


            SSLServerSocket serverSocket = new MServerSocketConfig().createSocket(7777,1);

            while (true) {
                try (SSLSocket socket = (SSLSocket) serverSocket.accept();
                     BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                    StringBuilder messageBuilder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        messageBuilder.append(line).append("\n");
                    }

                    String message = messageBuilder.toString().trim();
                    System.out.println("Nachricht empfangen:\n" + message);

                    MOutgoingMail mail = new MOutgoingMail("mail@example.com", "TLS-Mail")
                            .appendMessageText(message);
                    mailer.send(mail);
                }
            }

        } catch (Exception e) {
            System.err.println("Fehler im TLS-Mailer-Server: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}





