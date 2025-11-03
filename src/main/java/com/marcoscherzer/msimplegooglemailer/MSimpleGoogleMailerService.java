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

    /**
     * @author Marco Scherzer, Author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public static void main(String[] args) {
        try {
            //String pw = MConsoleUtil.promptPassword("Bitte Start Passwort eingeben:");
            String pw = "mypassword";
            MSimpleKeyStore store = new MSimpleKeyStore(System.getProperty("user.dir") + "\\mystore.jks", pw);

            checkStoreForExistingClientTokenOrReadItFromDirectoryAndDeleteFile(store);

            MSimpleGoogleMailer mailer = new MSimpleGoogleMailer(store, "BackupMailer");

            serverSocket = new MServerSocketConfig().createSocket(7777, 1);
            Runtime.getRuntime().addShutdownHook(new Thread(() -> { exit(); }));
            
            while (true) {
                    conSocket = (SSLSocket) serverSocket.accept();
                    reader = new BufferedReader(new InputStreamReader(conSocket.getInputStream()));
                    StringBuilder messageBuilder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        messageBuilder.append(line).append("\n");
                    }
                    String message = messageBuilder.toString().trim();
                    System.out.println("Nachricht empfangen:\n" + message);
                    MOutgoingMail mail = new MOutgoingMail("fromAdress","toAdress", "Mail").appendMessageText(message);
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
public static void checkStoreForExistingClientTokenOrReadItFromDirectoryAndDeleteFile(MSimpleKeyStore store) throws Exception {
    File jsonFile = new File(System.getProperty("user.dir"), "client_secret.json");

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
            throw new IllegalStateException("Kein client_secret.json gefunden und keine Client-Daten im Keystore. Bitte client_secret.json in " + System.getProperty("user.dir") + " ablegen.");
        }
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




