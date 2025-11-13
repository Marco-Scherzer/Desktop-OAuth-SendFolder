package com.marcoscherzer.oauthfilesendfolderforgooglemail;

import java.io.File;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
 * der in einem BatchFile eine FileListe per Pipe annehmen und per
 * file drag and drop auf sein folder symbol oder seinen (Desktop) Link die FileList an den ServerTeil schicken kann.
 */
public class MFileLinkCollectorFolderClient {

    private static Socket socket;
    private static PrintWriter out;
    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public static final void main(String[] args) {
        // Shutdown-Hook registrieren
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("ShutdownHook triggered – cleaning up resources...");
            exit(0);
        }));
        try {
            List<File> collectedFiles = new ArrayList<>();
            for (String path : args) {
                try {
                    Paths.get(path); // Syntaxprüfung
                    File f = new File(path);
                    if (f.exists()) {
                        collectedFiles.add(f);
                        System.out.println("collected paths: " + f.getAbsolutePath());
                    } else {
                        System.err.println("Paths not found: " + path);
                    }
                } catch (InvalidPathException ex) {
                    System.err.println("Invalid path: " + path);
                    exit(1);
                }
            }
            System.out.println("\nAll collected paths: " + collectedFiles.size());
            socket = new Socket("localhost", 11111);
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            for (File f : collectedFiles) out.println(f.getAbsolutePath());
            System.out.println("All Paths sent to Server");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
        new Scanner(System.in).nextLine(); // dbg
        exit(0);
    }
    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public static final void exit(int nr) {
        try {
            if (out != null) out.close();
            if (socket != null && !socket.isClosed()) socket.close();
            System.out.println("Resources closed. Exit code: " + nr);
        } catch (Exception e) {
            System.err.println("Error during exit: " + e.getMessage());
        }
    }
}
