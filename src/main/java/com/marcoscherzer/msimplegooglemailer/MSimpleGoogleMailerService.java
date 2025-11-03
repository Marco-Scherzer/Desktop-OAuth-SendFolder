package com.marcoscherzer.msimplegooglemailer;
import javax.net.ssl.*;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Marco Scherzer, Author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public class MSimpleGoogleMailerService {

    private static SSLServerSocket serverSocket;

    /**
     * @author Marco Scherzer, Author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public static void main(String[] args) {
        try {
            Path credPath = Paths.get(System.getProperty("user.dir"), "client_secret.json");
            MSimpleGoogleMailer mailer = new MSimpleGoogleMailer("Mailer", "1234567@outlook.com", credPath.toFile());

           serverSocket = new MServerSocketConfig().createSocket(7777,1);
           Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    serverSocket.close();
                    System.out.println("ServerSocket wurde geschlossen.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }));

            while (true) {
                try (SSLSocket socket = (SSLSocket) serverSocket.accept();
                     BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                    StringBuilder messageBuilder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) { messageBuilder.append(line).append("\n");}
                    String message = messageBuilder.toString().trim();
                    System.out.println("Nachricht empfangen:\n" + message);
                    MOutgoingMail mail = new MOutgoingMail("1234567@outlook.com", "Mail")
                            .appendMessageText(message);
                    mailer.send(mail);
                }
            }

        } catch (Exception e) {
            System.err.println("Fehler: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}





