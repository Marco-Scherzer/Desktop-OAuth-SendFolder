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

    private static final String userDir = System.getProperty("user.dir");
    private static final String basePath = userDir+"\\mail";
    private static String clientAndPathUUID;
    private static MFileWatcher watcher;
    private static Path sentFolder;
    private static String fromAdress;
    private static String toAdress;

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public static final void main(String[] args) {
        try {
            System.setErr(System.out);
            args = new String[]{"m.scherzer@outlook.com", "m.scherzer@outlook.com"};
            Path keystorePath = Paths.get(userDir, "mystore.p12");
            boolean initialized = Files.exists(keystorePath);
            boolean argsLengthOK = args.length == 2;

            if (!initialized && !argsLengthOK) throw new Exception("Error: On first start, you must setup the fromAddress and toAddress:\njava -jar MSendBackupMail [From-Email-Address] [To-Email-Address]");

            String pw = MUtil.promptPassword(!initialized ? "Please set a password: " : "Please enter your password: ");
          //  pw = "testTesttest-123";

            System.out.println();
            MSimpleGoogleMailer.setClientKeystoreDir(userDir);
            MSimpleGoogleMailer mailer = new MSimpleGoogleMailer("BackupMailer", pw, true);
            MSimpleKeystore store = mailer.getKeystore();

            if (!store.containsAllNonNullKeys("fromAddress", "toAddress")) {
                store.add("fromAddress", MUtil.checkMailAddress(args[0]));
                store.add("toAddress", MUtil.checkMailAddress(args[1]));
            }

            clientAndPathUUID = store.get("clientId");

            Path path= Paths.get(basePath, clientAndPathUUID);

            Path watchPath = createPathIfNotExists(path, "Backup directory");

            sentFolder = createPathIfNotExists(Paths.get(basePath,clientAndPathUUID + "-sent"), "Sent folder");

            fromAdress = store.get("fromAddress");
            toAdress = store.get("toAddress");


 /*
  *@author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
  */
            watcher = new MFileWatcher() {
                @Override
                protected void onFileChangedAndUnlocked(Path file) {
                    boolean sent = false;
                    try {
                        String sendTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                        MOutgoingMail mail = new MOutgoingMail(fromAdress, toAdress, clientAndPathUUID + ", sendTime " + sendTime)
                                .appendMessageText("Backup " + clientAndPathUUID + "\\" + file.getFileName())
                                .addAttachment(file.toString());

                        mailer.send(mail);
                        System.out.println("Backup sent: " + file.getFileName());
                        sent = true;
                    } catch (Exception sendExc) {
                        System.err.println("Error while sending: " + sendExc.getMessage());
                    }

                    if (sent) {
                        try {
                            Path targetFile = sentFolder.resolve(file.getFileName());
                            Files.move(file, targetFile, StandardCopyOption.REPLACE_EXISTING);
                            System.out.println("File moved to: " + targetFile);
                        } catch (Exception moveExc) {
                            System.err.println("Error while moving the file: " + moveExc.getMessage());
                        }
                    }
                }
            };

            watcher.startWatching(watchPath);
            System.out.println("New files added to the path will be automatically sent via email.");

        } catch (Exception exc) {
            Throwable cause = exc.getCause();
            //System.err.println("\nError: " + exc.getMessage());
            if (cause != null) System.err.println(cause.getMessage());
            exit(1);
        }
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public static final Path createPathIfNotExists(Path path, String descriptionName) throws Exception {
        try {
            if (!Files.exists(path)) {
                Files.createDirectories(path);
                System.out.println(descriptionName + " created: " + path);
            } else {
                System.out.println(descriptionName + " already exists: " + path);
            }
            return path;
        } catch (IOException e) {
            throw new Exception("Error while creating directory for \"" + descriptionName + "\": " + path, e);
        }
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private static void exit(int code) {
        try {
            if (watcher != null) watcher.shutdown();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        System.out.println("Program terminated. Exit code: " + code);
        System.exit(code);
    }
}







