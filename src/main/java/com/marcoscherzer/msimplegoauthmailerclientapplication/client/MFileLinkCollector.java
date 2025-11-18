package com.marcoscherzer.msimplegoauthmailerclientapplication;
import java.io.File;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.PrintStream;
import java.net.Socket;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public final class MFileLinkCollector {

    private static Socket socket;
    private static PrintWriter out;
    private static boolean alreadyCalledExit;

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public static final void main(String[] args) {
        PrintStream logStream;
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if(!alreadyCalledExit) {System.out.println("ShutdownHook triggered – cleaning up resources...");exit(0);}
        }));

        try {
            logStream = new PrintStream(System.getProperty("user.dir")+System.getProperty("file.separator")+"MLinkCollectorFolderClient.log");
            System.setOut(logStream);System.setErr(logStream);
        } catch (Exception e) {
            System.err.println("Error: log File LinkCollectorFolderClient.log could not be created."+ e.getMessage());
            System.err.println("Continuing without redirecting Logging to LogFile.\" ");
        }

        try {
            List<File> collectedFileLinks = new ArrayList<>();
            for (String path : args) {
                try {
                    Paths.get(path); // Syntaxprüfung
                    File f = new File(path);
                    if (f.exists()) {
                        collectedFileLinks.add(f);
                        System.out.println("collected paths: " + f.getAbsolutePath());
                    } else { System.err.println("Paths not found: " + path);}
                } catch (InvalidPathException ex) {System.err.println("Invalid path: " + path);exit(1);}
            }

            if (!collectedFileLinks.isEmpty()) {
                System.out.println("\nAll collected paths: " + collectedFileLinks.size());
                socket = new Socket("localhost", 11111);
                out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
                for (File f : collectedFileLinks) out.println(f.getAbsolutePath());
                out.flush();
                socket.shutdownOutput(); // Sending EOF to Server
                System.out.println("All Paths sent to Server");
            } else { System.out.println("No paths to send to the Server. Nothing to do");}
        } catch (Exception e) {System.err.println("Error: " + e.getMessage());exit(1);}
        exit(0);
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public static final void exit(int nr) {
        alreadyCalledExit=true;
        try {
            if (out != null) out.close();
            if (socket != null && !socket.isClosed()) socket.close();
            System.out.println("Resources closed. Exit code: " + nr);
        } catch (Exception e) {
            System.err.println("Error during exit: " + e.getMessage());
        }
    }
}
