/**
 * @author Marco Scherzer, Author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
package com.marcoscherzer.msimplegooglemailer;

import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;

import javax.net.ssl.*;
import java.io.*;

/**
 * @author Marco Scherzer, Author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public class MSimpleGoogleMailerService {

    private static SSLServerSocket serverSocket;
    private static BufferedReader reader;
    private static SSLSocket conSocket;
    private static MSimpleKeyStore store;

    /**
     * @author Marco Scherzer, Author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public static void main(String[] args) {
        try {
            //String pw = MConsoleUtil.promptPassword("Bitte Start Passwort eingeben:");
            String pw = "mypassword";
            String fromAdress ="Marco.Scherzer@outlook.com"; String toAdress =fromAdress;

            store = new MSimpleKeyStore(System.getProperty("user.dir") + "\\mystore.jks", pw);

            MSimpleGoogleMailer mailer = new MSimpleGoogleMailer(store, "BackupMailer");

            serverSocket = new MServerSocketConfig().createSocket(store,7777, 1);
            Runtime.getRuntime().addShutdownHook(new Thread(() -> { exit(); }));
            
            while (true) {
                conSocket = (SSLSocket) serverSocket.accept();
                System.out.println("Verbindung akzeptiert von: " + conSocket.getInetAddress());
                System.out.println("SSLSession: " + conSocket.getSession().getProtocol());
                reader = new BufferedReader(new InputStreamReader(conSocket.getInputStream()));
                    StringBuilder messageBuilder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        messageBuilder.append(line).append("\n");
                    }
                    String message = messageBuilder.toString().trim();
                    System.out.println("Nachricht empfangen:\n" + message);
                    MOutgoingMail mail = new MOutgoingMail(fromAdress,toAdress, "Mail").appendMessageText(message);
                    mailer.send(mail);
            }


        } catch (Exception e) {
            System.err.println("Fehler: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }


    /**
     * @author Marco Scherzer, Author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
public static final void exit() {
    try {
        reader.close();
        serverSocket.close();
        System.out.println("ServerSocket wurde geschlossen.");
    } catch (IOException e) {
        e.printStackTrace();
    }
}

}




