package com.marcoscherzer.msimplekeystore;

/**
 * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public final class MClientSecretException extends Throwable{
    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MClientSecretException(String txt){
        super(txt);
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MClientSecretException(String txt, Exception exc){
        super(txt, exc);
    }
}
