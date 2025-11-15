package com.marcoscherzer.msimplegooglemailer;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Marco Scherzer, Author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public final class MOutgoingMail {
    private String from;
    private String to;
    private String subject;
    private final StringBuilder messageText;
    private final List<String> attachmentList = new ArrayList<>();

    /**
     * @author Marco Scherzer, Author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MOutgoingMail(String from, String to) {
        MSimpleGoogleMailerUtil.checkMailAddress(from);
        MSimpleGoogleMailerUtil.checkMailAddress(to);
        this.from = from;
        this.to = to;
        this.messageText = new StringBuilder();
    }

    /**
     * @author Marco Scherzer, Author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final MOutgoingMail setTo(String to){
        this.to = to;
        return this;
    }

    /**
     * @author Marco Scherzer, Author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final MOutgoingMail setFrom(String from){
        this.from = from;
        return this;
    }

    /**
     * @author Marco Scherzer, Author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final MOutgoingMail setSubject(String subject){
        this.subject = subject;
        return this;
    }

    /**
     * @author Marco Scherzer, Author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final MOutgoingMail appendMessageText(String text) {
        if (text != null && !text.isBlank()) {
            messageText.append(text);
        }
        return this;
    }

    /**
     * @author Marco Scherzer, Author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final MOutgoingMail addAttachment(String file) {
        if (file != null && !file.isBlank()) {
            attachmentList.add(file);
        }
        return this;
    }

    /**
     * @author Marco Scherzer, Author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final String[] getAttachments() {
        return attachmentList.toArray(new String[0]);
    }

    /**
     * @author Marco Scherzer, Author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final String getAttachment(int index) {
        return attachmentList.get(index);
    }

    /**
     * @author Marco Scherzer, Author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final String getMessageText() {
        return messageText.toString();
    }
    /**
     * @author Marco Scherzer, Author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final String getFrom() {
        return from;
    }
    /**
     * @author Marco Scherzer, Author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final String getTo() {
        return to;
    }
    /**
     * @author Marco Scherzer, Author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final String getSubject() {
        return subject;
    }
}
