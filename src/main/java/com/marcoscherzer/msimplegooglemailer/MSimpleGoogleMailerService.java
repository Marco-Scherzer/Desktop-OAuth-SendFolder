package com.marcoscherzer.msimplegooglemailer;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public class MSimpleGoogleMailerService {
    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public static void main(String[] args) {
        try {
            String pw = MUtil.promptPassword("Bitte Passwort eingeben:");
            Runtime.getRuntime().addShutdownHook(new Thread(() -> System.exit(0)));

            MSimpleKeyStore store = new MSimpleKeyStore(System.getProperty("user.dir") + "\\mystore.jks", pw);
            MSimpleGoogleMailer mailer = new MSimpleGoogleMailer(store, "BackupMailer");

            String fromAddress = "Marco.Scherzer@outlook.com";
            String toAddress = fromAddress;

            Path watchPath = Paths.get("D:/Backup/Android_Backup/rar");
            long delayMs = 30000; // 30 Sekunden

            MFileWatcherWithDelayHandling watcher = new MFileWatcherWithDelayHandling() {
                @Override
                protected void onFileChanged(Path file) {
                    try {
                        MOutgoingMail mail = new MOutgoingMail(fromAddress, toAddress, "Backup: " + file.getFileName())
                                .appendMessageText("Automatischer Versand")
                                .addAttachment(file.toString());

                        mailer.send(mail);
                        System.out.println("Backup versendet: " + file.getFileName());
                    } catch (Exception e) {
                        System.err.println("Fehler beim Senden: " + e.getMessage());
                    }
                }
            };

            watcher.startWatching(watchPath, delayMs);

        } catch (Exception e) {
            System.err.println("Fehler: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}





