package com.marcoscherzer.msimplegooglemailer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
/**
 * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public class MSimpleGoogleMailerService {
    private static String clientAndPathUUID;
    private static MFileWatcher watcher;
    private static String userDir= System.getProperty("user.dir");

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public static void main(String[] args) {
        try {
            args = new String[]{ System.getProperty("user.dir")+"\\testBackupBasePath" , "marco.scherzer@outlook.com", "marco.scherzer@outlook.com"};
            Path keystorePath = Paths.get(userDir, "mystore.p12");
            boolean initialized = Files.exists(keystorePath);
            boolean argsLengthOK = args.length == 3;

            if (!initialized && ! argsLengthOK)
                throw new Exception("Bei der ersten Verwendung muessen der Basis Pfad, sowie fromAddress und toAddress gesetzt werden:" +
                        "\njava -jar MSendBackupMail [Base-Path] [From-Email-Adress] [To-Email-Adress]");
            // sonst Keystore erzeugen

            String pw = MUtil.promptPassword(!initialized ? "Bitte Passwort setzen: " : "Bitte Passwort eingeben: ");
            MSimpleGoogleMailer.setClientKeystoreDir(userDir);
            MSimpleGoogleMailer mailer = new MSimpleGoogleMailer("BackupMailer", pw, true);
            //Keystore erzeugt, UUID und alles andere intern gesetzt
            MSimpleKeyStore store = mailer.getKeystore();

            //nicht vorhandener initialParamter als Indikator für das setzen der initialParameter
            if (store.contains("basePath")){
                store.addToken("basePath", args[0]);
                store.addToken("fromAddress", MUtil.checkMailAddress(args[1]));
                store.addToken("toAddress", MUtil.checkMailAddress(args[2]));
            }

            // UUID aus dem Keystore lesen
            String uuid = store.getToken("clientId");
            Path watchPath = Paths.get(store.getToken("basePath"), uuid);

            if (!Files.exists(watchPath)) {
                Files.createDirectories(watchPath);
                System.out.println("Backup-Verzeichnis erstellt: " + watchPath);
            } else {
                System.out.println("Gespeicherter Backup-Pfad: " + watchPath);
            }

            MSimpleGoogleMailerService.clientAndPathUUID = uuid;
            watcher = new MFileWatcher() {
                @Override
                protected void onFileChangedAndUnlocked(Path file) {
                    try {
                        MOutgoingMail mail = new MOutgoingMail(store.getToken("fromAddress"),"toAddress", clientAndPathUUID)
                                .appendMessageText("Automatischer Versand von " + clientAndPathUUID + "\\" + file.getFileName())
                                .addAttachment(file.toString());

                        mailer.send(mail);
                        System.out.println("Backup versendet: " + file.getFileName());
                    } catch (Exception exc) {
                        System.err.println("Fehler beim Senden: " + exc.getMessage());
                    }
                }
            };

            watcher.startWatching(watchPath);
            System.out.println("Neue Dateien, die dem Pfad hinzugefügt werden, werden automatisch per E-Mail versendet.");
            exit(0);
        } catch (Exception exc) {
            System.err.println("\n\nFehler: " + exc.getMessage());
            //e.printStackTrace();
            exit(1);
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






