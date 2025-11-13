package com.marcoscherzer.oauthfilesendfolderforgooglemail;

import com.marcoscherzer.msimplegooglemailer.MOutgoingMail;
import com.marcoscherzer.msimplegooglemailer.MSimpleGoogleMailer;

import java.io.File;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.marcoscherzer.oauthfilesendfolderforgooglemail.MUtil.createFolderDesktopLink;
import static com.marcoscherzer.oauthfilesendfolderforgooglemail.MUtil.createFolderLink;

/**
 * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 * unready
 */
public abstract class MConsentOutgoingMailWatcher extends MFolderWatcher {

    private final String fromAddress;
    private final String toAddress;
    private final String clientAndPathUUID;
    private final boolean askConsent;
    private final Path notSentFolder;
    private final MSimpleGoogleMailer mailer;
    private final Path sentFolder;
    private volatile boolean userConsentActive = false;
    private final List<String> fileLinkList = new ArrayList<>();
    private int attachedFilesCnt;

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

    public void onNewAttachmentList() {

    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private void sendAndCollect(MOutgoingMail mail) {
        String sendDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        mail.setSubject(clientAndPathUUID + ", sendTime " + sendDateTime);

        try {
            for (String path : fileLinkList) {
                File file = new File(path);
                mail.addAttachment(path);
                attachedFilesCnt++;
                mail.appendMessageText(clientAndPathUUID + "\\" + file.getName());
            }
            try {
                showSendGui(mail, this.send(), this::moveAllToFolder(notSentFolder));
            } catch (Exception sendFail) {
                System.err.println("Mail send failed: " + sendFail.getMessage());
            }

            System.out.println("Mail sent with " + attachedFilesCnt + " attachments.");
        } catch (Exception sendExc) {
            System.err.println("Error during send: " + sendExc.getMessage());
        }
    }
    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private final void moveAllToFolder(Path folder){
        for (String path : fileLinkList) {
            File sourceFile = new File(path);
            String newName = MUtil.createTimeStampFileName(sourceFile);
            try {
                createFolderLink(sourceFile.getPath(), folder, newName );
                System.out.println("Sent and moved: " + newName);
            } catch (Exception exc) {
                System.err.println("Failed to move unsent file: " + sourceFile.getName() + " " + exc.getMessage());
            }
    }
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    abstract void showSendGui();

}




