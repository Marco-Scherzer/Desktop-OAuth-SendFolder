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
   Client-Server Architektur mit einem Client
 * der in einem BatchFile eine FileListe per Pipe annehmen oder per
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
            List<File> collectedFileLinks = new ArrayList<>();
            for (String path : args) {
                try {
                    Paths.get(path); // Syntaxprüfung
                    File f = new File(path);
                    if (f.exists()) {
                        collectedFileLinks.add(f);
                        System.out.println("collected paths: " + f.getAbsolutePath());
                    } else {
                        System.err.println("Paths not found: " + path);
                    }
                } catch (InvalidPathException ex) {
                    System.err.println("Invalid path: " + path);
                    exit(1);
                }
            }
            if(!collectedFileLinks.isEmpty()) {
                System.out.println("\nAll collected paths: " + collectedFileLinks.size());
                socket = new Socket("localhost", 11111);
                out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
                for (File f : collectedFileLinks) out.println(f.getAbsolutePath());
                out.flush();
                //Sending EOF to Server
                socket.shutdownOutput();
                System.out.println("All Paths sent to Server");
            } else System.out.println("No paths to send to the Server. Nothing to do");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            exit(1);
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
