package com.marcoscherzer.msimplekeystore;

/**
 * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public final class MPasswordIntegrityException extends Throwable{

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MPasswordIntegrityException(String txt){
        super(txt);
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MPasswordIntegrityException(String txt, Exception exc){
        super(txt, exc);
    }
}
