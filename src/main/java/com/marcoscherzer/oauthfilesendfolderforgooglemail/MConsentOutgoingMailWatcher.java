package com.marcoscherzer.oauthfilesendfolderforgooglemail;

import com.marcoscherzer.msimplegooglemailer.MOutgoingMail;
import com.marcoscherzer.msimplegooglemailer.MSimpleGoogleMailer;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;

/**
 * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public abstract class MConsentOutgoingMailWatcher extends MFolderWatcher{

    private final String fromAddress;
    private final String toAddress;
    private final String clientAndPathUUID;
    private final boolean askConsent;
    private final Path notSentFolder;
    private final MSimpleGoogleMailer mailer;
    private final Path sentFolder;
    private volatile boolean userConsentActive = false;
    private final ScheduledExecutorService consentTimer = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> pendingSessionEnd = null;
    private final List<MOutgoingMail> toSendMails = Collections.synchronizedList(new ArrayList<>());
    private final List<MOutgoingMail> sentMails = Collections.synchronizedList(new ArrayList<>());
    private final long sessionIdleMillis = 15000;
    private final Object consentLock = new Object();

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MConsentOutgoingMailWatcher(Path outFolder, Path sentFolder, Path notSentFolder, MSimpleGoogleMailer mailer,
                                       String fromAddress,
                                       String toAddress,
                                       String clientAndPathUUID,
                                       boolean askConsent) {
        super(outFolder);
        this.sentFolder = sentFolder;
        this.fromAddress = fromAddress;
        this.toAddress = toAddress;
        this.clientAndPathUUID = clientAndPathUUID;
        this.askConsent = askConsent;
        this.notSentFolder = notSentFolder;
        this.mailer = mailer;
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public void onFileChangedAndUnlocked(Path file) {
        if (!Files.exists(file)) {
            System.out.println("User deleted file. Nothing to send.");
            return;
        }

        String sendDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        MOutgoingMail mail = new MOutgoingMail(fromAddress, toAddress)
                .setSubject(clientAndPathUUID + ", sendTime " + sendDateTime)
                .appendMessageText(clientAndPathUUID + "\\" + file.getFileName())
                .addAttachment(file.toString());

        synchronized (consentLock) {
            if (!askConsent || userConsentActive) {
                toSendMails.add(mail);
                scheduleSessionEnd();
                return;
            }

            if (showSendAlert(mail)) {
                userConsentActive = true;
                toSendMails.add(mail);
                scheduleSessionEnd();
            } else {
                try {
                    Path targetFile = notSentFolder.resolve(file.getFileName());
                    Files.move(file, targetFile, StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("User denied sending mail. File moved to NotSent: " + targetFile);
                } catch (Exception moveExc) {
                    System.err.println("Error while moving to NotSent: " + moveExc.getMessage());
                }
            }
        }
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private void scheduleSessionEnd() {
        if (pendingSessionEnd != null && !pendingSessionEnd.isDone()) {
            pendingSessionEnd.cancel(false);
        }

        pendingSessionEnd = consentTimer.schedule(() -> {
            synchronized (consentLock) {
                List<MOutgoingMail> successfullySent = new ArrayList<>();

                try {

                    SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS");
                    for (MOutgoingMail ml : toSendMails) {
                        File sourceFile = new File(ml.getAttachment(0));
                        String originalName = sourceFile.getName();
                        String extension = "";
                        int dotIndex = originalName.lastIndexOf('.');
                        if (dotIndex != -1) {
                            extension = originalName.substring(dotIndex);
                            originalName = originalName.substring(0, dotIndex);
                        }
                        String timestamp = timestampFormat.format(new Date());
                        String newName = originalName + "_" + timestamp + extension;

                        try {
                            //mailer.send(ml);

                            File targetFile = new File(sentFolder.toFile(), newName);
                            Files.move(sourceFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                            System.out.println("Sent and moved: " + newName);

                            successfullySent.add(ml);
                            sentMails.add(ml);
                        } catch (Exception e) {
                            try {
                                File fallbackFile = new File(notSentFolder.toFile(), newName);
                                Files.move(sourceFile.toPath(), fallbackFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                                System.err.println("Send failed, moved to NOT SENT: " + newName);
                            } catch (IOException moveFail) {
                                System.err.println("Failed to move unsent file: " + sourceFile.getName() + " -> " + moveFail.getMessage());
                            }
                            System.err.println("Error sending: " + sourceFile.getName() + " -> " + e.getMessage());
                        }
                    }

                    toSendMails.removeAll(successfullySent);
                    System.out.println("Mails sent: " + successfullySent.size());
                } catch (Exception sendExc) {
                    System.err.println("Error during session end: " + sendExc.getMessage());
                }

                userConsentActive = false;
            }
        }, sessionIdleMillis, TimeUnit.MILLISECONDS);
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    protected abstract boolean showSendAlert(MOutgoingMail mail);


}
