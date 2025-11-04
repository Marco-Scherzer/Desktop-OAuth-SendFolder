package com.marcoscherzer.msimplegooglemailer;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public class MSimpleGoogleMailerService {
    private static String clientAndPathUUID;

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public static void main(String[] args) {
        try {
            args[0]= System.getProperty("user.dir"+"\\testBackupBasePath");
            String fromAddress = "mailaddr@...";
            String toAddress = fromAddress;
            long delayMs = 30000; // 30 Sekunden
            Path keystorePath = Paths.get(System.getProperty("user.dir"), "mystore.p12");

            if (!Files.exists(keystorePath) && args.length != 1) {
                throw new Exception("Bei der ersten Verwendung muss der Basis Pfad gesetzt werden: java -jar MSendBackupMail [Basis-Pfad]");
            }

            String pw = MUtil.promptPassword("Bitte Passwort eingeben:");
            Runtime.getRuntime().addShutdownHook(new Thread(() -> System.exit(0)));

            MSimpleGoogleMailer.setClientKeystoreDir(System.getProperty("user.dir"));
            MSimpleGoogleMailer mailer = new MSimpleGoogleMailer("BackupMailer", pw, false);
            MSimpleKeyStore store = mailer.getKeystore();

            // UUID aus dem Keystore lesen
            String uuid = store.getToken("clientId");
            if (uuid == null || uuid.isBlank()) {
                throw new IllegalStateException("Keine clientId im Keystore gefunden.");
            }

            if(args.length==1 && !store.contains("basePath")) store.addToken("basePath",args[0]);
            Path watchPath = Paths.get(store.getToken("basePath"), uuid);

            if (!Files.exists(watchPath)) {
                Files.createDirectories(watchPath);
                System.out.println("Backup-Verzeichnis erstellt: " + watchPath);
            } else {
                System.out.println("Gespeicherter Backup-Pfad: " + watchPath);
            }

            MSimpleGoogleMailerService.clientAndPathUUID = uuid;

            MFileWatcherWithDelayHandling watcher = new MFileWatcherWithDelayHandling() {
                @Override
                protected void onFileChanged(Path file) {
                    try {
                        MOutgoingMail mail = new MOutgoingMail(fromAddress, toAddress, clientAndPathUUID)
                                .appendMessageText("Automatischer Versand von " + clientAndPathUUID + "\\" + file.getFileName())
                                .addAttachment(file.toString());

                        //mailer.send(mail);
                        System.out.println("Backup versendet: " + file.getFileName());
                    } catch (Exception e) {
                        System.err.println("Fehler beim Senden: " + e.getMessage());
                    }
                }
            };

            watcher.startWatching(watchPath, delayMs);
            System.out.println("Neue Dateien, die dem Pfad hinzugefügt werden, werden automatisch per E-Mail versendet.");

        } catch (Exception e) {
            System.err.println("Fehler: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}






