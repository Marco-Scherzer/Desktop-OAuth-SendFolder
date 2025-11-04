package com.marcoscherzer.msimplegooglemailer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
/**
 * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public class MSimpleGoogleMailerService {
    private static String uuid;
    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public static void main(String[] args) {
        try {
            String pw = MUtil.promptPassword("Bitte Passwort eingeben:");
            Runtime.getRuntime().addShutdownHook(new Thread(() -> System.exit(0)));

            MSimpleKeyStore store = new MSimpleKeyStore(System.getProperty("user.dir") + "\\mystore.jks", pw);
            MSimpleGoogleMailer mailer = new MSimpleGoogleMailer(store, "BackupMailer");

            String fromAddress = "mailadresse@...";
            String toAddress = fromAddress;

            Path watchPath;
            uuid="";
            if (!store.contains("backupPath")) {
                if (args.length != 1) {
                    throw new Exception("Verwendung: java -jar MSendBackupMail.jar [Basis-Backup-Pfad]");
                }

                String basePath = args[0];
                uuid = UUID.randomUUID().toString();
                System.out.println("Zusätzliche Sicherheits-Pfad-UUID wird erstellt (erschwert Missbrauch): " + uuid);

                watchPath = Paths.get(basePath, uuid);

                if (!Files.exists(watchPath)) {
                    Files.createDirectories(watchPath);
                    System.out.println("Backup-Verzeichnis erstellt: " + watchPath);
                }

                store.addToken("backupPath", watchPath.toString());
                System.out.println("Backup-Pfad initialisiert: " + watchPath);
            } else {
                watchPath = Paths.get(store.getToken("backupPath"));
                System.out.println("Gespeicherter Backup-Pfad: " + watchPath);
            }

            long delayMs = 30000; // 30 Sekunden

            MFileWatcherWithDelayHandling watcher = new MFileWatcherWithDelayHandling() {
                @Override
                protected void onFileChanged(Path file) {
                    try {
                        MOutgoingMail mail = new MOutgoingMail(fromAddress, toAddress, uuid)
                                .appendMessageText("Automatischer Versand von "+uuid+"\\" + file.getFileName())
                                .addAttachment(file.toString());

                        mailer.send(mail);
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






