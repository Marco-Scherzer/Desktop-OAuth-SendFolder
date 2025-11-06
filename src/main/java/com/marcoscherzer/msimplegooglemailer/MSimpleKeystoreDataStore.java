package com.marcoscherzer.msimplegooglemailer;

import com.google.api.client.util.store.DataStore;
import com.google.api.client.util.store.DataStoreFactory;

import java.io.IOException;
import java.io.Serializable;
import java.security.GeneralSecurityException;
import java.util.*;

/**
 * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public class MSimpleKeystoreDataStore<V extends Serializable> implements DataStore<V>, DataStoreFactory {

    private final MSimpleKeystore keystore;
    private final String id;

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MSimpleKeystoreDataStore(MSimpleKeystore keystore, String id) {
        this.keystore = keystore;
        this.id = id;
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public static DataStoreFactory getDataStoreFactory(MSimpleKeystore keystore) {
        return new MSimpleKeystoreDataStore<>(keystore, "default");
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    @Override
    public DataStoreFactory getDataStoreFactory() {
        return this;
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    @Override
    public DataStore<V> getDataStore(String id) throws IOException {
        return new MSimpleKeystoreDataStore<>(keystore, id);
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    @Override
    public int size() throws IOException {
        return keySet().size();
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    @Override
    public boolean isEmpty() throws IOException {
        return size() == 0;
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    @Override
    public boolean containsKey(String key) throws IOException {
        return keystore.contains(key);
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    @Override
    public boolean containsValue(V value) throws IOException {
        return values().contains(value);
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    @Override
    public Set<String> keySet() throws IOException {
        return Collections.emptySet(); // Optional: implement if needed
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    @Override
    public Collection<V> values() throws IOException {
        return Collections.emptyList(); // Optional: implement if needed
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    @Override
    public V get(String key) throws IOException {
        try {
            String raw = keystore.get(key);
            return raw == null ? null : (V) raw;
        } catch (GeneralSecurityException e) {
            throw new IOException("Fehler beim Lesen aus Keystore", e);
        }
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    @Override
    public DataStore<V> set(String key, V value) throws IOException {
        try {
            keystore.add(key, value.toString());
            return this;
        } catch (Exception e) {
            throw new IOException("Fehler beim Schreiben in Keystore", e);
        }
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    @Override
    public DataStore<V> delete(String key) throws IOException {
        try {
            keystore.remove(key);
            return this;
        } catch (Exception e) {
            throw new IOException("Fehler beim Entfernen aus Keystore", e);
        }
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    @Override
    public DataStore<V> clear() throws IOException {
        try {
            keystore.clear();
            return this;
        } catch (Exception e) {
            throw new IOException("Fehler beim Löschen des Keystores", e);
        }
    }
}

