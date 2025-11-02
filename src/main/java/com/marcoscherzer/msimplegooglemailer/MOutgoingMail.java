package com.marcoscherzer.msimplegooglemailer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Marco Scherzer, Author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public class MOutgoingMail {
    public String to;
    public String subject;
    private final StringBuilder messageText;
    private final List<String> attachmentList = new ArrayList<>();

    /**
     * @author Marco Scherzer, Author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MOutgoingMail(String to, String subject) {
        MUtil.checkMailAddress(to);
        this.to = to;
        this.subject = subject;
        this.messageText = new StringBuilder();
    }

    /**
     * @author Marco Scherzer, Author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MOutgoingMail appendMessageText(String text) {
        if (text != null && !text.isBlank()) {
            messageText.append(text);
        }
        return this;
    }

    /**
     * @author Marco Scherzer, Author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MOutgoingMail addAttachment(String file) {
        if (file != null && !file.isBlank()) {
            attachmentList.add(file);
        }
        return this;
    }

    /**
     * @author Marco Scherzer, Author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public String[] getAttachments() {
        return attachmentList.toArray(new String[0]);
    }

    /**
     * @author Marco Scherzer, Author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public String getMessageText() {
        return messageText.toString();
    }
}