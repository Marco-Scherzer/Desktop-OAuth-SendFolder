package com.marcoscherzer.msimplegoauthmailer;

public class MMailAdressFormatException extends Throwable {

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    MMailAdressFormatException(String txt){
        super(txt);
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    MMailAdressFormatException(String txt, Exception exc){
        super(txt, exc);
    }
}
