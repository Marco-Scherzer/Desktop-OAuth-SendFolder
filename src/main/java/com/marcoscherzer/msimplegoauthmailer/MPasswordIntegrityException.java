package com.marcoscherzer.msimplegoauthmailer;

/**
 * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public final class MPasswordIntegrityException extends Exception{

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    MPasswordIntegrityException(String txt){
        super(txt);
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    MPasswordIntegrityException(String txt, Exception exc){
        super(txt, exc);
    }
}
