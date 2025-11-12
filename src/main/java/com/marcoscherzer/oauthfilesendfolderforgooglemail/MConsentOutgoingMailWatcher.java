package com.marcoscherzer.oauthfilesendfolderforgooglemail;

import com.marcoscherzer.msimplegooglemailer.MOutgoingMail;
import com.marcoscherzer.msimplegooglemailer.MSimpleGoogleMailer;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
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
    private final List<String> pendingAttachments = Collections.synchronizedList(new ArrayList<>());
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
    @Override
    public void onFileChangedAndUnlocked(Path file) {
        if (!Files.exists(file)) {
            System.out.println("User deleted file. Nothing to send.");
            return;
        }
        synchronized (consentLock) {
            pendingAttachments.add(file.toString());

            if (!askConsent || userConsentActive) {
                scheduleSessionEnd();
                return;
            }

            if (showSendAlert(null)) {
                userConsentActive = true;
                scheduleSessionEnd();
            } else {
                try {
                    Path targetFile = notSentFolder.resolve(file.getFileName());
                    Files.move(file, targetFile, StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("User denied sending mail. File moved to NotSent: " + targetFile);
                    pendingAttachments.remove(file.toString());
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
                if (pendingAttachments.isEmpty()) return;

                String sendDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                MOutgoingMail mail = new MOutgoingMail(fromAddress, toAddress)
                        .setSubject(clientAndPathUUID + ", sendTime " + sendDateTime);

                List<String> successfullySentPaths = new ArrayList<>();

                try {
                    for (String path : pendingAttachments) {
                        File file = new File(path);
                        mail.addAttachment(path);
                        mail.appendMessageText(clientAndPathUUID + "\\" + file.getName());
                    }

                    try {
                        mailer.send(mail);
                    } catch (Exception sendFail) {
                        System.err.println("Mail send failed: " + sendFail.getMessage());
                    }

                    for (String path : pendingAttachments) {
                        File sourceFile = new File(path);
                        String newName = MUtil.createTimeStampFileName(sourceFile);
                        try {
                            File targetFile = new File(sentFolder.toFile(), newName);
                            Files.move(sourceFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                            System.out.println("Sent and moved: " + newName);
                            successfullySentPaths.add(path);
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

                    sentMails.add(mail);
                    pendingAttachments.removeAll(successfullySentPaths);
                    System.out.println("Mail sent with " + successfullySentPaths.size() + " attachments.");
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
