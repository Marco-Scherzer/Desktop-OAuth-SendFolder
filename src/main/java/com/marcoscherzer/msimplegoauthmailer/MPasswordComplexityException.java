package com.marcoscherzer.msimplegoauthmailer;

/**
 * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public final class MPasswordComplexityException extends Throwable{

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    MPasswordComplexityException(String txt){
        super(txt);
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    MPasswordComplexityException(String txt, Exception exc){
        super(txt, exc);
    }
}
