package com.marcoscherzer.msimplegoauthmailer;

public class MKeystoreException extends Throwable {
    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    MKeystoreException(String txt){
        super(txt);
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    MKeystoreException(String txt, Exception exc){
        super(txt, exc);
    }
}
