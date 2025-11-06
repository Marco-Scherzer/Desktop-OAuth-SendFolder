package com.marcoscherzer.msimplegooglemailer;

/**
 * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public class MPasswordIncorrectException extends Exception {
    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MPasswordIncorrectException(String message, Exception exc) {
        super(message, exc);
    }
}
