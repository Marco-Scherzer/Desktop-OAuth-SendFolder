package com.marcoscherzer.msimplegooglemailer;

import com.google.api.client.util.store.DataStore;
import com.google.api.client.util.store.DataStoreFactory;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.*;

/**
 * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public class MSimpleKeystoreDataStore<V extends Serializable> implements DataStore<V> {

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
    public DataStoreFactory getDataStoreFactory() {
        return new MSimpleKeystoreDataStoreFactory(keystore);
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
            String base64 = keystore.get(key);
            return base64 == null ? null : deserialize(base64);
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
            String base64 = serialize(value);
            keystore.add(key, base64);
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

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private String serialize(V value) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(value);
            return Base64.getEncoder().encodeToString(baos.toByteArray());
        }
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private V deserialize(String base64) throws IOException {
        byte[] data = Base64.getDecoder().decode(base64);
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data))) {
            return (V) ois.readObject();
        } catch (ClassNotFoundException e) {
            throw new IOException("Deserialisierung fehlgeschlagen", e);
        }
    }
}



