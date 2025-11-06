package com.marcoscherzer.msimplegooglemailer;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public final class MSimpleGoogleMailerService {
    private static String clientAndPathUUID;
    private static MFileWatcher watcher;
    private static String userDir= System.getProperty("user.dir");
    private static Path sentFolder;
    private static String fromAdress;
    private static String toAdress;

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public static final void main(String[] args) {
        try {
            args = new String[]{ System.getProperty("user.dir")+"\\testBackupBasePath" , "fromAddress", "toAddress"};
            Path keystorePath = Paths.get(userDir, "mystore.p12");
            boolean initialized = Files.exists(keystorePath);
            boolean argsLengthOK = args.length == 3;

            if (!initialized && ! argsLengthOK)
                throw new Exception("Bei der ersten Verwendung muessen der Basis Pfad, sowie fromAddress und toAddress gesetzt werden:" +
                        "\njava -jar MSendBackupMail [Base-Path] [From-Email-Adress] [To-Email-Adress]");
            // sonst Keystore erzeugen oder vorhandenen verwenden

            String pw = MUtil.promptPassword(!initialized ? "Bitte Passwort setzen: " : "Bitte Passwort eingeben: ");
            pw="lkjhjgD-767548";

            System.out.println();
            MSimpleGoogleMailer.setClientKeystoreDir(userDir);
            MSimpleGoogleMailer mailer = new MSimpleGoogleMailer("BackupMailer", pw, true);
            //Keystore erzeugt, UUID und alles andere intern gesetzt
            MSimpleKeystore store = mailer.getKeystore();

            //nicht vorhandener initialParamter als Indikator für das setzen der initialParameter
            if (!store.isCompletelyInitialized("basePath","fromAddress","toAddress")){
                store.add("basePath", args[0]);
                store.add("fromAddress", MUtil.checkMailAddress(args[1]));
                store.add("toAddress", MUtil.checkMailAddress(args[2]));
            }
            // UUID aus dem Keystore lesen
            String uuid = store.get("clientId");

            Path watchPath = createPathIfNotExists(Paths.get(store.get("basePath"), uuid), "Backup-Verzeichnis");
            sentFolder = createPathIfNotExists(Paths.get(store.get("basePath"), store.get("clientId") + "-sent"), "sentFolder-Verzeichnis");

            clientAndPathUUID = uuid;
            fromAdress =store.get("fromAddress");
            toAdress = store.get("toAddress");
            watcher = new MFileWatcher() {
                @Override
                protected void onFileChangedAndUnlocked(Path file) {
                    boolean sent = false;
                    try {
                    String sendTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    MOutgoingMail mail = new MOutgoingMail(fromAdress, toAdress,clientAndPathUUID + ", sendTime " + sendTime)
                            .appendMessageText("Backup " + clientAndPathUUID + "\\" + file.getFileName())
                            .addAttachment(file.toString());

                        mailer.send(mail);
                        System.out.println("Backup versendet: " + file.getFileName());
                        sent = true;
                    } catch (Exception sendExc) {
                        System.err.println("Fehler beim Senden: " + sendExc.getMessage());
                    }

                    if (sent) {
                        try {
                            Path targetFile = sentFolder.resolve(file.getFileName());
                            Files.move(file, targetFile, StandardCopyOption.REPLACE_EXISTING);
                            System.out.println("Datei verschoben nach: " + targetFile);
                        } catch (Exception moveExc) {
                            System.err.println("Fehler beim Verschieben der Datei: " + moveExc.getMessage());
                        }
                    }
                }

            };
            watcher.startWatching(watchPath);
            System.out.println("Neue Dateien, die dem Pfad hinzugefügt werden, werden automatisch per E-Mail versendet.");
        } catch (Exception exc) {
            Throwable cause = exc.getCause();
            System.err.println("\nFehler: " + exc.getMessage());
            if(cause != null) System.err.println(exc.getCause().getMessage());
            //exc.printStackTrace();
            exit(1);
        }
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     * Beendet das Programm mit dem angegebenen Exit-Code.
     */
    public static Path createPathIfNotExists(Path path, String descriptionName) {
        try {
            if (!Files.exists(path)) {
                Files.createDirectories(path);
                System.out.println( descriptionName+ " erstellt: " + path);
            } else {
                System.out.println(descriptionName + " Pfad exitiert bereits: " + path);
            }
            return path;
        } catch (IOException e) {
            throw new RuntimeException("Fehler beim Erstellen des Verzeichnisses für \"" + descriptionName + "\": " + path, e);
        }
    }


    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     * Beendet das Programm mit dem angegebenen Exit-Code.
     */
    private static void exit(int code) {
        try { if(watcher != null )watcher.shutdown(); } catch (Exception exc) { exc.printStackTrace(); }
        System.out.println("Programm beendet. ExitCode: " + code);
        System.exit(code);
    }
}






