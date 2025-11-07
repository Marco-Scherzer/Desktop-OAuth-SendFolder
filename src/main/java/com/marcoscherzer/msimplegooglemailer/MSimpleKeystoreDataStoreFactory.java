package com.marcoscherzer.msimplegooglemailer;
import com.google.api.client.util.store.DataStore;
import com.google.api.client.util.store.DataStoreFactory;

import java.io.IOException;
import java.io.Serializable;

/**
 * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public final class MSimpleKeystoreDataStoreFactory implements DataStoreFactory {

    private final MSimpleKeystore keystore;

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MSimpleKeystoreDataStoreFactory(MSimpleKeystore keystore) {
        this.keystore = keystore;
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    @Override
    public final <V extends Serializable> DataStore<V> getDataStore(String id) throws IOException {
        return new MSimpleKeystoreDataStore<>(keystore, id);
    }
}

