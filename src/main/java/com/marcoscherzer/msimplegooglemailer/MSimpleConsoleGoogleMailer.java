package com.marcoscherzer.msimplegooglemailer;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author Marco Scherzer, Author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public class MSimpleConsoleGoogleMailer {
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

            MOutgoingMail mail = new MOutgoingMail("mail@example.com", "Testmail")
                    .appendMessageText("Hallo,\n\nDies ist eine automatisch generierte Testmail.");
                    //.addAttachment("Z:/MarcoScherzer-Projects/MSendBackupMail/testdaten/beispiel.txt"); // Optionaler Anhang

            File credFile = new File(System.getProperty("user.dir")+"\\client_secret.json");
            String appName = "theRegisteredGoogleServiceConsoleAppName";

            MSimpleGoogleMailer mailer = new MSimpleGoogleMailer(appName, "anEmailAddressValidForMyGoogleMailAccount", credFile);
            mailer.send(mail);

        } catch (Exception exc) {
            exc.printStackTrace();
            System.exit(1);
        }
    }

}

