package com.marcoscherzer.msimplegoauthmailerclientapplication;

import com.marcoscherzer.msimplegoauthmailer.MMailAdressFormatException;
import com.marcoscherzer.msimplegoauthmailer.MOutgoingMail;
import com.marcoscherzer.msimplegoauthmailer.MSimpleMailer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.marcoscherzer.msimplegoauthmailerclientapplication.MUtil.createFolderLink;

/**
 * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 * unready
 */
public abstract class MAttachmentWatcher {


        private final String fromAddress;
        private final String toAddress;
        private final String clientAndPathUUID;
        private final Path sentFolder;
        private final Path notSentFolder;
        private final MSimpleMailer mailer;
        private final List<String> fileLinkList = new ArrayList<>();
        private int attachedFilesCnt;

        // Server-Attribute
        private final ExecutorService serverMainLoop = Executors.newSingleThreadExecutor();
        private final ExecutorService pool = Executors.newCachedThreadPool();
        private ServerSocket serverSocket;
        private boolean busy;

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     * unready
     */
        public MAttachmentWatcher(Path sentFolder, Path notSentFolder, MSimpleMailer mailer, String fromAddress, String toAddress, String clientAndPathUUID) {
            this.sentFolder = sentFolder;
            this.fromAddress = fromAddress;
            this.toAddress = toAddress;
            this.clientAndPathUUID = clientAndPathUUID;
            this.notSentFolder = notSentFolder;
            this.mailer = mailer;
        }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     * unready
     */
        private static abstract class MJob1A<T> implements Runnable{
            protected T attribute1;
            public MJob1A(T attribute1){
             this.attribute1=attribute1;
            }
        }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     * unready
     */
    public final MAttachmentWatcher startServer() throws IOException {
        serverSocket = new ServerSocket(11111);
        serverMainLoop.submit(() -> {
            System.out.println("AttachmentWatcher listening on port 11111...");
            while (!serverSocket.isClosed()) {
                try {
                    Socket client = serverSocket.accept();
                    System.out.println("New incoming attachment list");
                    pool.submit(new MJob1A<Socket>(client) {
                        @Override
                        public void run() {
                            Socket client = attribute1;
                            BufferedReader reader = null;
                            try {
                                reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                                List<String> incomingPaths = new ArrayList<>();
                                String line;
                                while ((line = reader.readLine()) != null) {
                                    String path = line.trim();
                                    System.out.println("adding path \"" + path + "\"");
                                    incomingPaths.add(path);
                                }
                                try {
                                    if (reader != null) reader.close();
                                    if (client != null && !client.isClosed()) client.close();
                                } catch (IOException closeExc) {
                                    System.err.println("Error closing client resources: " + closeExc.getMessage());
                                }
                                System.out.println("client.isClosed() = " + client.isClosed());
                                System.out.println("incomingPaths.isEmpty() = " + incomingPaths.isEmpty());//dbg
                                System.out.println("creating Mail...");
                                onNewAttachmentList(incomingPaths);
                            } catch (IOException | MMailAdressFormatException e) {
                                System.err.println("Error in MailJob: " + e.getMessage());
                                try {
                                    if (reader != null) reader.close();
                                    if (client != null && !client.isClosed()) client.close();
                                } catch (IOException closeExc) {
                                    System.err.println("Error closing client resources after exception: " + closeExc.getMessage());
                                }
                            }
                        }
                    });
                } catch (IOException exc) {
                    System.err.println("Error in server loop: " + exc.getMessage());
                }
            }
        });
        return this;
    }
    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     * unready
     */
    public final void shutdown() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing server socket: " + e.getMessage());
        }
        pool.shutdownNow();
        serverMainLoop.shutdownNow();
        System.out.println("AttachmentWatcher shutdown complete.");
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     * unready
     */
        private void onNewAttachmentList(List<String> list) throws MMailAdressFormatException {
            fileLinkList.clear();
            fileLinkList.addAll(list);
            String sendDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            MOutgoingMail mail = new MOutgoingMail(fromAddress, toAddress)
                    .setSubject(clientAndPathUUID + ", sendTime " + sendDateTime);

            try {
                attachedFilesCnt = 0;
                for (String path : fileLinkList) {
                    File file = new File(path);
                    mail.addAttachment(path);
                    attachedFilesCnt++;
                    mail.appendMessageText(clientAndPathUUID + "\\" + file.getName());
                }

                MConsentQuestioner r = askForConsent(mail); // modal
                while(r.getResult()==null){
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException exc) {
                        System.err.println("Error: "+ exc.getMessage());
                    }
                }

                if (r.getResult()) {
                    mailer.send(mail);
                    moveAllToFolder(sentFolder);
                } else {
                    moveAllToFolder(notSentFolder);
                }

                System.out.println("Mail processed with " + attachedFilesCnt + " attachments.");
            } catch (Exception sendExc) {
                System.err.println("Error during send: " + sendExc.getMessage());
            }
        }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     * unready
     */
        private void moveAllToFolder(Path folder) {
            for (String path : fileLinkList) {
                File sourceFile = new File(path);
                String newName = MUtil.createTimeStampFileName(sourceFile);
                try {
                    createFolderLink(sourceFile.getPath(), folder, newName);
                    System.out.println("Moved: " + newName);
                } catch (Exception exc) {
                    System.err.println("Failed to move file: " + sourceFile.getName() + " " + exc.getMessage());
                }
            }
        }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     * unready
     */
        // Abstrakte Methode für Consent
        public abstract MConsentQuestioner askForConsent(MOutgoingMail mail);
    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     * unready
     */
        public interface MConsentQuestioner {
            Boolean getResult();
        }

    }





