package com.marcoscherzer.msimplegooglemailer;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.*;
import java.security.cert.CertificateException;

/**
 * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public final class MSimpleKeystore {

    private final File ksFile;
    private final String keystorePassword;
    private static final String KEYSTORE_TYPE = "PKCS12";
    private KeyStore keyStore;

    public MSimpleKeystore(File ksFile, String keystorePassword) {
        this.ksFile = ksFile;
        this.keystorePassword = keystorePassword;
    }

    public final boolean loadKeyStoreOrCreateKeyStoreIfNotExists() throws RuntimeException, MPasswordIncorrectException {
        try { keyStore = KeyStore.getInstance(KEYSTORE_TYPE); } catch (Exception exc) { throw new RuntimeException("Error in context with keystore while instantiating keystore", exc); }

        if (ksFile.exists()) {
            System.out.println("loading keystore " + ksFile);
            FileInputStream fis = null;
            try { fis = new FileInputStream(ksFile); } catch (Exception exc) { throw new RuntimeException("Error in context with keystore while opening keystore file", exc); }
            try { keyStore.load(fis, keystorePassword.toCharArray()); } catch (IOException exc) {
                Throwable cause = exc.getCause();
                if (cause instanceof UnrecoverableKeyException || cause instanceof UnrecoverableEntryException || cause instanceof BadPaddingException) {
                    throw new MPasswordIncorrectException("Incorrect password", exc);
                }
                if (cause instanceof IllegalBlockSizeException) {
                    throw new RuntimeException("Error in context with keystore while decrypting block (possible password or corruption)", exc);
                }
                if (cause instanceof EOFException) {
                    throw new RuntimeException("Error in context with keystore while reading file (unexpected end of file)", exc);
                }
                throw new RuntimeException("Error in context with keystore while loading existing keystore", exc);
            } catch (Exception exc) { throw new RuntimeException("Error in context with keystore while loading existing keystore", exc); }
            try { fis.close(); } catch (Exception exc) { throw new RuntimeException("Error in context with keystore while closing keystore file", exc); }
            return false;
        } else {
            System.out.println("creating keystore " + ksFile);
            try { keyStore.load(null, keystorePassword.toCharArray()); } catch (Exception exc) { throw new RuntimeException("Error in context with keystore while initializing new keystore", exc); }
            try { ksFile.getParentFile().mkdirs(); } catch (Exception exc) { throw new RuntimeException("Error in context with keystore while creating parent directories", exc); }
            FileOutputStream fos = null;
            try { fos = new FileOutputStream(ksFile); } catch (Exception exc) { throw new RuntimeException("Error in context with keystore while opening fileoutputstream", exc); }
            try { keyStore.store(fos, keystorePassword.toCharArray()); } catch (Exception exc) { throw new RuntimeException("Error in context with keystore while storing new keystore", exc); }
            try { fos.close(); } catch (Exception exc) { throw new RuntimeException("Error in context with keystore while closing fileoutputstream", exc); }
            return true;
        }
    }

    public final KeyStore getKeyStore() {
        return keyStore;
    }

    public final MSimpleKeystore add(String alias, String token) throws RuntimeException {
        SecretKey secretKey;
        try { secretKey = new SecretKeySpec(token.getBytes(), "AES"); } catch (Exception exc) { throw new RuntimeException("Error in context with keystore while creating secret key", exc); }
        KeyStore.SecretKeyEntry entry = new KeyStore.SecretKeyEntry(secretKey);
        KeyStore.ProtectionParameter protParam = new KeyStore.PasswordProtection(keystorePassword.toCharArray());
        try { keyStore.setEntry(alias, entry, protParam); } catch (Exception exc) { throw new RuntimeException("Error in context with keystore while setting entry " + alias, exc); }
        FileOutputStream fos = null;
        try { fos = new FileOutputStream(ksFile); } catch (Exception exc) { throw new RuntimeException("Error in context with keystore while opening fileoutputstream", exc); }
        try { keyStore.store(fos, keystorePassword.toCharArray()); } catch (Exception exc) { throw new RuntimeException("Error in context with keystore while storing after add", exc); }
        try { fos.close(); } catch (Exception exc) { throw new RuntimeException("Error in context with keystore while closing fileoutputstream", exc); }
        return this;
    }

    public final boolean isCompletelyInitialized(String... keys) throws RuntimeException {
        for (String key : keys) {
            String value;
            try { value = get(key); } catch (Exception exc) { throw new RuntimeException("Error in context with keystore while checking key " + key, exc); }
            if (value == null || value.isBlank()) return false;
        }
        return true;
    }

    public final String get(String alias) throws RuntimeException {
        KeyStore.ProtectionParameter protParam = new KeyStore.PasswordProtection(keystorePassword.toCharArray());
        KeyStore.SecretKeyEntry entry;
        try { entry = (KeyStore.SecretKeyEntry) keyStore.getEntry(alias, protParam); } catch (Exception exc) { throw new RuntimeException("Error in context with keystore while retrieving entry " + alias, exc); }
        if (entry == null) return null;
        SecretKey secretKey;
        try { secretKey = entry.getSecretKey(); } catch (Exception exc) { throw new RuntimeException("Error in context with keystore while accessing secret key for " + alias, exc); }
        return new String(secretKey.getEncoded());
    }

    public final boolean contains(String alias) throws RuntimeException {
        try { return keyStore.containsAlias(alias); } catch (Exception exc) { throw new RuntimeException("Error in context with keystore while checking contains " + alias, exc); }
    }

    public final boolean remove(String key) throws RuntimeException {
        boolean contains;
        try { contains = keyStore.containsAlias(key); } catch (Exception exc) { throw new RuntimeException("Error in context with keystore while checking contains " + key, exc); }
        if (contains) {
            try { keyStore.deleteEntry(key); } catch (Exception exc) { throw new RuntimeException("Error in context with keystore while deleting entry " + key, exc); }
            FileOutputStream fos = null;
            try { fos = new FileOutputStream(ksFile); } catch (Exception exc) { throw new RuntimeException("Error in context with keystore while opening fileoutputstream", exc); }
            try { keyStore.store(fos, keystorePassword.toCharArray()); } catch (Exception exc) { throw new RuntimeException("Error in context with keystore while storing keystore", exc); }
            try { fos.close(); } catch (Exception exc) { throw new RuntimeException("Error in context with keystore while closing fileoutputstream", exc); }
            return true;
        }
        return false;
    }

    public final void clear() throws RuntimeException {
        java.util.Enumeration<String> aliases;
        try { aliases = keyStore.aliases(); } catch (Exception exc) { throw new RuntimeException("Error in context with keystore while listing aliases", exc); }
        while (aliases.hasMoreElements()) {
            String alias = aliases.nextElement();
            try { keyStore.deleteEntry(alias); } catch (Exception exc) { throw new RuntimeException("Error in context with keystore while deleting entry " + alias, exc); }
        }
        FileOutputStream fos = null;
        try { fos = new FileOutputStream(ksFile); } catch (Exception exc) { throw new RuntimeException("Error in context with keystore while opening fileoutputstream", exc); }
        try { keyStore.store(fos, keystorePassword.toCharArray()); } catch (Exception exc) { throw new RuntimeException("Error in context with keystore while storing after clear", exc); }
        try { fos.close(); } catch (Exception exc) { throw new RuntimeException("Error in context with keystore while closing fileoutputstream", exc); }
    }
}
