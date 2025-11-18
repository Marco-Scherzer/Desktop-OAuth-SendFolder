package com.marcoscherzer.msimplekeystore;

public class MKeystoreException extends Throwable {
    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MKeystoreException(String txt){
        super(txt);
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MKeystoreException(String txt, Exception exc){
        super(txt, exc);
    }
}
