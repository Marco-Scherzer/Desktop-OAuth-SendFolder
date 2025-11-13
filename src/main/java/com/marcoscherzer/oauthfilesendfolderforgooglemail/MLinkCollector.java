package com.marcoscherzer.oauthfilesendfolderforgooglemail;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 * Da die komplexität mit Einführung einer consent gui(sicherheitsgründe) overhead hat
 * ist eigentlich auch der zusatzspeicherverbrauch durch einkopieren in die Windows folder
 * nun nicht mehr hinnehmbar. Dies war für einfache ein-attachment-pro mail dinge sinnvoll,
 * da der code einfach blieb und backuper oder batch files einfach in die folder schreiben konnten.
 * Neben dem Speicherverbauchsnachteil hat
 * die Komplexität des Folderwatchings im Kontext der Gui nun aber mehr Aufwand als eine kleine Client-Server
 * Architektur einzuführen und den Speichernachteil von Anfang an vermeidet.
 * Dieses File beginnt vor Entwicklungbeginn durchdachten Plan B: Client-Server Architektur mit einem Client
 * der mindestens in einem BatchFile eine FileListe annehmen kann.
 * Da jedoch ohnehin keine Netzwerkfunktionalität benötigt wird(die mit folders durchs system gegeben gewesen wäre)
 * wird diese spontane Projekt aus Zeitgründen nun abgegekürzt und die main methoden schnittstelle von MSimpleGoogleMailerService
 * per file drag and drop als Schnittstelle zum Filelinks sammeln Verwendung finden.
 * Sollte auch mit einem Programm-link auf dem Desktop funktionieren.
 */
public class MLinkCollector {

    // Liste der eingesammelten Dateien
    private final List<File> collectedFiles = new ArrayList<>();

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public void addFiles(String[] paths) {
        for (String path : paths) {
            File f = new File(path);
            if (f.exists()) {
                collectedFiles.add(f);
                System.out.println("Eingesammelt: " + f.getAbsolutePath());
            } else {
                System.err.println("Pfad nicht gefunden: " + path);
            }
        }
        System.out.println("Gesamt eingesammelt: " + collectedFiles.size());
    }

}
