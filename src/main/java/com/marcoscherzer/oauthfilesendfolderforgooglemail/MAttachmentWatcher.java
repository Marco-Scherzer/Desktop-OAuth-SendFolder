package com.marcoscherzer.oauthfilesendfolderforgooglemail;

import com.marcoscherzer.msimplegooglemailer.MOutgoingMail;
import com.marcoscherzer.msimplegooglemailer.MSimpleGoogleMailer;

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

import static com.marcoscherzer.oauthfilesendfolderforgooglemail.MUtil.createFolderLink;

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
        private final MSimpleGoogleMailer mailer;
        private final List<String> fileLinkList = new ArrayList<>();
        private int attachedFilesCnt;

        // Server-Attribute
        private final ExecutorService executor = Executors.newSingleThreadExecutor();
        private ServerSocket serverSocket;
        private boolean busy;

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     * unready
     */
        public MAttachmentWatcher(Path sentFolder, Path notSentFolder, MSimpleGoogleMailer mailer, String fromAddress, String toAddress, String clientAndPathUUID) {
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
    public final MAttachmentWatcher startServer() throws IOException {
        serverSocket = new ServerSocket(11111);
        executor.submit(() -> {
            System.out.println("AttachmentWatcher listening on port 11111...");
            while (!serverSocket.isClosed()) {
                Socket client = null;
                BufferedReader reader = null;
                try {
                    client = serverSocket.accept();
                  if(!busy) {
                      busy = true;
                      reader = new BufferedReader(new InputStreamReader(client.getInputStream()));

                      List<String> incomingPaths = new ArrayList<>();
                      String line;
                      while ((line = reader.readLine()) != null) {
                          incomingPaths.add(line.trim());
                      }

                      if (!incomingPaths.isEmpty()) {
                          onNewAttachmentList(incomingPaths);
                      }
                  } else System.out.println("Server buisy");
                } catch (IOException e) {
                    if (!serverSocket.isClosed()) {
                        System.err.println("Error in server loop: " + e.getMessage());
                    }
                } finally {
                    try {
                        if (reader != null) reader.close();
                        if (client != null && !client.isClosed()) client.close();
                    } catch (IOException closeExc) {
                        System.err.println("Error closing client resources: " + closeExc.getMessage());
                    }
                }
                busy = false;
            }
        });
        return this;
    }


    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     * unready
     */
        private void onNewAttachmentList(List<String> list) {
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

                MConsentQuestionResult r = askForConsent(mail); // modal
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
        public final void shutdown() {
            try {
                if (serverSocket != null && !serverSocket.isClosed()) {
                    serverSocket.close();
                }
            } catch (IOException e) {
                System.err.println("Error closing server socket: " + e.getMessage());
            }
            executor.shutdownNow();
            System.out.println("AttachmentWatcher shutdown complete.");
        }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     * unready
     */
        // Abstrakte Methode für Consent
        public abstract MConsentQuestionResult askForConsent(MOutgoingMail mail);
    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     * unready
     */
        public interface MConsentQuestionResult {
            boolean getResult();
        }

    }





