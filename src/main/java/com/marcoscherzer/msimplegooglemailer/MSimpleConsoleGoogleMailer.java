package com.marcoscherzer.msimplegooglemailer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.nio.file.StandardWatchEventKinds.*;

/**
 * @author Marco Scherzer, Author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public class MSimpleConsoleGoogleMailer {

    private static final ExecutorService pool = Executors.newSingleThreadExecutor();

    /**
     * @author Marco Scherzer, Author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public static MOutgoingMail createOutgoingMail(String[] args) throws FileNotFoundException {
        if (args.length < 3) {
            System.err.println("Verwendung: java MSimpleConsoleGoogleMailer <Empfänger> <Betreff> <Nachricht> [Anhänge...]");
            throw new IllegalArgumentException("Zu wenige Argumente übergeben.");
        }

        for (int i = 3; i < args.length; i++) {
            Path p = Path.of(args[i]);
            if (!Files.exists(p)) {
                System.err.println("Fehler: Anhang nicht gefunden -> " + args[i]);
                throw new FileNotFoundException("Datei nicht gefunden: " + args[i]);
            }
        }

        MOutgoingMail mail = new MOutgoingMail(args[0], args[1])
                .appendMessageText(args[2]);

        for (int i = 3; i < args.length; i++) {
            mail.addAttachment(args[i]);
        }

        return mail;
    }

    /**
     * @author Marco Scherzer, Author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public static void main(String[] args) {
        try {
            File credFile = new File(System.getProperty("user.dir") + "/client_secret.json");
            String appName = "theRegisteredGoogleServiceConsoleAppName";
            String path = System.getProperty("user.home") + "/.backupmailer/tokens.jks";

            MSimpleGoogleMailer mailer = new MSimpleGoogleMailer(appName, "anEmailAddressValidForMyGoogleMailAccount", credFile);

            RandomAccessFile file = new RandomAccessFile("shared.mmf", "r");
            WatchService watchService = FileSystems.getDefault().newWatchService();
            Paths.get(System.getProperty("user.dir")).register(watchService, ENTRY_MODIFY, ENTRY_CREATE, ENTRY_DELETE);

            /**
             * @author Marco Scherzer, Author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
             */
            pool.submit(new Runnable() {
                /**
                 * @author Marco Scherzer, Author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
                 */
                public final void run() {
                    try {
                        while (true) {
                            WatchKey key = watchService.take();

                            for (WatchEvent<?> event : key.pollEvents()) {
                                WatchEvent.Kind<?> kind = event.kind();
                                Path changed = (Path) event.context();

                                if (changed.toString().equals("shared.mmf")) {
                                    String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                                    System.out.println("Datei geändert: " + changed + " [" + kind.name() + "] @ " + timestamp);

                                    FileChannel channel = file.getChannel();
                                    MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, 1024);
                                    int length = buffer.getInt();
                                    byte[] data = new byte[length];
                                    buffer.get(data);
                                    String message = new String(data);

                                    System.out.println("Nachricht empfangen: " + message);

                                    MOutgoingMail mail = new MOutgoingMail("mail@example.com", "Testmail")
                                            .appendMessageText(message);
                                    mailer.send(mail);
                                }
                            }

                            boolean valid = key.reset();
                            if (!valid) {
                                System.out.println("WatchKey ungültig. Beende Überwachung.");
                                break;
                            }
                        }
                    } catch (Exception e) {
                        System.err.println("Fehler im WatchLoop: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            });

        } catch (Exception exc) {
            exc.printStackTrace();
            System.exit(1);
        }
    }
}


