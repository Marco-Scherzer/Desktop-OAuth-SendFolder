package com.marcoscherzer.msimplegoauthmailer.msimplekeystoredatastoreadapter;

import com.google.api.client.util.store.DataStore;
import com.google.api.client.util.store.DataStoreFactory;
import com.marcoscherzer.msimplekeystore.MKeystoreException;
import com.marcoscherzer.msimplekeystore.MSimpleKeystore;

import java.io.*;
import java.util.*;

/**
 * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public final class MSimpleKeystoreDataStore<V extends Serializable> implements DataStore<V> {

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
    public final DataStoreFactory getDataStoreFactory() {
        return new MSimpleKeystoreDataStoreFactory(keystore);
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    @Override
    public final String getId() {
        return id;
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    @Override
    public final int size() throws IOException {
        return keySet().size();
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    @Override
    public final boolean isEmpty() throws IOException {
        return size() == 0;
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    @Override
    public final boolean containsKey(String key) throws IOException {
        try { return keystore.contains(key); } catch (Exception exc) { throw new IOException(exc); }
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    @Override
    public final boolean containsValue(V value) throws IOException {
        return values().contains(value);
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    @Override
    public final Set<String> keySet() throws IOException {
        Set<String> keys = new HashSet<>();
        try {
            Enumeration<String> aliases = keystore.keySet();
            while (aliases.hasMoreElements()) {
                keys.add(aliases.nextElement());
            }
            return keys;
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    @Override
    public final Collection<V> values() throws IOException {
        List<V> values = new ArrayList<>();
        for (String key : keySet()) {
            V value = get(key);
            if (value != null) values.add(value);
        }
        return values;
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    @Override
    public final V get(String key) throws IOException {
        try {
            String base64 = keystore.get(key);
            return base64 == null ? null : deserialize(base64);
        } catch (Exception exc) {
            throw new IOException(exc);
        }
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    @Override
    public final DataStore<V> set(String key, V value) throws IOException {
        try {
            String base64 = serialize(value);
            keystore.put(key, base64);
            return this;
        } catch (Exception exc) {
            throw new IOException(exc);
        }
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    @Override
    public final DataStore<V> delete(String key) throws IOException {
        try {
            keystore.remove(key);
            return this;
        } catch (Exception exc) {
            throw new IOException(exc);
        }
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    @Override
    public final DataStore<V> clear() throws IOException {
        try {
            keystore.clear();
            return this;
        } catch (Exception exc) {
            throw new IOException(exc);
        }
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private final String serialize(V value) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos;
        try { oos = new ObjectOutputStream(baos); } catch (Exception e) { throw new IOException("Error creating ObjectOutputStream", e); }
        try { oos.writeObject(value); } catch (Exception e) { throw new IOException("Error serializing object", e); }
        try { oos.close(); } catch (Exception e) { throw new IOException("Error closing ObjectOutputStream", e); }
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private final V deserialize(String base64) throws IOException {
        byte[] data = Base64.getDecoder().decode(base64);
        ObjectInputStream ois;
        try { ois = new ObjectInputStream(new ByteArrayInputStream(data)); } catch (Exception e) { throw new IOException("Error creating ObjectInputStream", e); }
        try {
            return (V) ois.readObject();
        } catch (ClassNotFoundException e) {
            throw new IOException("Deserialization failed", e);
        } catch (Exception e) {
            throw new IOException("Error reading object from stream", e);
        } finally {
            try { ois.close(); } catch (Exception e) { throw new IOException("Error closing ObjectInputStream", e); }
        }
    }
}




