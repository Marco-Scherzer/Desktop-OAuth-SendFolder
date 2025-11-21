package com.marcoscherzer.msimplekeystore;

/**
 * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public final class MPasswordComplexityException extends Exception{

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MPasswordComplexityException(String txt){
        super(txt);
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MPasswordComplexityException(String txt, Exception exc){
        super(txt, exc);
    }
}
